import tensorflow as tf

# 主模型
class hmt_grn(tf.keras.Model):
    def __init__(self, arg):
        super().__init__()

        self.hidden_dim = arg['hidden_dim']
        self.embedding_dim = arg['embedding_dim']
        self.num_classes = arg['num_classes']
        self.spatialGraph = arg['spatialGraph']
        self.temporalGraph = arg['temporalGraph']
        self.poi_len = [len(arg['geohash2Index_2']), len(arg['geohash2Index_3']), len(arg['geohash2Index_4']),
                        len(arg['geohash2Index_5']), len(arg['geohash2Index_6'])]
        self.padding_idx = 0

        self.dropout = tf.keras.layers.Dropout(arg['dropout'])
        # float32
        self.temporalGAT = multiHeadAttention(arg, 'poi')
        # float32
        self.spatialGAT = multiHeadAttention(arg, 'poi')

        self.poiEmbed = tf.keras.layers.Embedding(arg['vocab_size'], arg['embedding_dim'], mask_zero=True)
        self.geoHashEmbed2 = tf.keras.layers.Embedding(len(arg['index2geoHash_2']), arg['embedding_dim'], mask_zero=True)
        self.geoHashEmbed3 = tf.keras.layers.Embedding(len(arg['index2geoHash_3']), arg['embedding_dim'], mask_zero=True)
        self.geoHashEmbed4 = tf.keras.layers.Embedding(len(arg['index2geoHash_4']), arg['embedding_dim'], mask_zero=True)
        self.geoHashEmbed5 = tf.keras.layers.Embedding(len(arg['index2geoHash_5']), arg['embedding_dim'], mask_zero=True)
        self.geoHashEmbed6 = tf.keras.layers.Embedding(len(arg['index2geoHash_6']), arg['embedding_dim'], mask_zero=True)
        self.userEmbed = tf.keras.layers.Embedding(arg['numUsers'], arg['userEmbed_dim'], mask_zero=True)

        self.nextGeoHash2Dense = tf.keras.layers.Dense(len(arg['geohash2Index_2']), use_bias=True)
        self.nextGeoHash3Dense = tf.keras.layers.Dense(len(arg['geohash2Index_3']), use_bias=True)
        self.nextGeoHash4Dense = tf.keras.layers.Dense(len(arg['geohash2Index_4']), use_bias=True)
        self.nextGeoHash5Dense = tf.keras.layers.Dense(len(arg['geohash2Index_5']), use_bias=True)
        self.nextGeoHash6Dense = tf.keras.layers.Dense(len(arg['geohash2Index_6']), use_bias=True)

        self.fuseDense = tf.keras.layers.Dense(arg['num_classes'] * 1, use_bias=True)

        self.ownLSTM = ownLSTM(arg['embedding_dim'] * 1, arg['hidden_dim'])


    def call(self, inputs, training=None):
        if training:
            x, users, y, x_geoHash2, x_geoHash3, x_geoHash4, x_geoHash5, x_geoHash6 = inputs
            x, users, y = tf.cast(x, tf.float32), tf.cast(users, tf.float32), tf.cast(y, tf.float32)

            numTimeSteps = len(x[0])
        else:
            x, users, x_geoHash2, x_geoHash3, x_geoHash4, x_geoHash5, x_geoHash6 = inputs
            x, users, x_geoHash2, x_geoHash3, x_geoHash4, x_geoHash5, x_geoHash6 = tf.cast(x, tf.float32), \
                                                                                   tf.cast(users, tf.float32), \
                                                                                   tf.cast(x_geoHash2, tf.float32), \
                                                                                   tf.cast(x_geoHash3, tf.float32), \
                                                                                   tf.cast(x_geoHash4, tf.float32), \
                                                                                   tf.cast(x_geoHash5, tf.float32), \
                                                                                   tf.cast(x_geoHash6, tf.float32)
            users = tf.expand_dims(users, axis=0)
            numTimeSteps = len(x)

        batchSize = len(x)

        x_embed_allTimeStep = self.poiEmbed(x)
        x_embed_allTimeStep = tf.reshape(x_embed_allTimeStep, [batchSize, numTimeSteps, self.embedding_dim])

        finalAttendedOutSpatial = list()
        finalAttendedOutTemporal = list()

        for eachBatchIndex in range(batchSize):

            currentSample = x[eachBatchIndex]
            if training == False:
                currentSample = tf.expand_dims(currentSample, axis=0)
            singleBatchSpatial = list()
            singleBatchTemporal = list()

            for timeStep in range(len(currentSample)):
                sample = int(currentSample[timeStep])

                spatialNiegh = self.spatialGraph.get(str(sample))
                # AtlasView(
                #     {23: {}, 24: {}, 26: {}, 27: {}, 46: {}, 47: {}, 48: {}, 49: {}, 51: {}, 52: {}, 53: {}, 54: {},

                try:
                    temporalNiegh = self.temporalGraph.get[str(sample)]
                except:
                    # as the temporal graph is based only on training set, there might be new POIs not found in the graph.
                    # in that case, we use only the POI itself as neighbour.
                    temporalNiegh = [sample]

                x_embed = self.poiEmbed(tf.cast([sample], tf.float32))

                if sample == 0:  # padding
                    spatial_GAT_out = x_embed
                    temporal_GAT_out = x_embed
                else:
                    spatialNieghEmbed = tf.expand_dims(self.poiEmbed(tf.cast(spatialNiegh, tf.float32)), axis=0)
                    temporalNieghEmbed = tf.expand_dims(self.poiEmbed(tf.cast(temporalNiegh, tf.float32)), axis=0)

                    spatial_GAT_out = self.spatialGAT(x_embed, spatialNieghEmbed, numTimeSteps, users, 'repeat')
                    temporal_GAT_out = self.temporalGAT(x_embed, temporalNieghEmbed, numTimeSteps, users, 'repeat')

                singleBatchSpatial.append(spatial_GAT_out)
                singleBatchTemporal.append(temporal_GAT_out)

            singleBatchSpatialAll = tf.stack(singleBatchSpatial, axis=1)
            finalAttendedOutSpatial.append(singleBatchSpatialAll)

            singleBatchTemporalAll = tf.stack(singleBatchTemporal, axis=1)
            finalAttendedOutTemporal.append(singleBatchTemporalAll)

        finalAttendedOutSpatialAll = tf.squeeze(tf.stack(finalAttendedOutSpatial, axis=1), axis=0)
        finalAttendedOutTemporalAll = tf.squeeze(tf.stack(finalAttendedOutTemporal, axis=1), axis=0)
        x_geoHash_embed2 = self.dropout(self.geoHashEmbed2(x_geoHash2), training=training)
        x_geoHash_embed3 = self.dropout(self.geoHashEmbed3(x_geoHash3), training=training)
        x_geoHash_embed4 = self.dropout(self.geoHashEmbed4(x_geoHash4), training=training)
        x_geoHash_embed5 = self.dropout(self.geoHashEmbed5(x_geoHash5), training=training)
        x_geoHash_embed6 = self.dropout(self.geoHashEmbed6(x_geoHash6), training=training)

        rnn_out, _ = self.ownLSTM(x_embed_allTimeStep, finalAttendedOutSpatialAll, finalAttendedOutTemporalAll)

        userEmbedSeq = self.userEmbed(users)
        userEmbedSeq = self.dropout(userEmbedSeq, training=training)

        rnn_out = self.dropout(rnn_out, training=training)
        finalEmbed = tf.concat((rnn_out, userEmbedSeq), axis=2)

        nextGeoHashlogits_2 = self.nextGeoHash2Dense(tf.concat((rnn_out, x_geoHash_embed2), axis=2))
        nextGeoHashlogits_3 = self.nextGeoHash3Dense(tf.concat((rnn_out, x_geoHash_embed3), axis=2))
        nextGeoHashlogits_4 = self.nextGeoHash4Dense(tf.concat((rnn_out, x_geoHash_embed4), axis=2))
        nextGeoHashlogits_5 = self.nextGeoHash5Dense(tf.concat((rnn_out, x_geoHash_embed5), axis=2))
        nextGeoHashlogits_6 = self.nextGeoHash6Dense(tf.concat((rnn_out, x_geoHash_embed6), axis=2))

        nextGeoHashlogits_2 = tf.reshape(nextGeoHashlogits_2, [batchSize, numTimeSteps, self.poi_len[0]])
        nextGeoHashlogits_3 = tf.reshape(nextGeoHashlogits_3, [batchSize, numTimeSteps, self.poi_len[1]])
        nextGeoHashlogits_4 = tf.reshape(nextGeoHashlogits_4, [batchSize, numTimeSteps, self.poi_len[2]])
        nextGeoHashlogits_5 = tf.reshape(nextGeoHashlogits_5, [batchSize, numTimeSteps, self.poi_len[3]])
        nextGeoHashlogits_6 = tf.reshape(nextGeoHashlogits_6, [batchSize, numTimeSteps, self.poi_len[4]])

        logits = self.fuseDense(finalEmbed)

        logits = tf.reshape(logits, [batchSize, numTimeSteps, self.num_classes])

        return logits, nextGeoHashlogits_2, nextGeoHashlogits_3, nextGeoHashlogits_4, nextGeoHashlogits_5, nextGeoHashlogits_6

# 自注意力层
class selfAttention(tf.keras.layers.Layer):
    def __init__(self, arg, type):
        super(selfAttention, self).__init__()
        if type == 'user':
            dim = arg['userEmbed_dim']
        elif type == 'poi':
            dim = arg['embedding_dim']

        self.embedding_dim = dim
        self.attentionDense = tf.keras.layers.Dense(dim, kernel_initializer='glorot_normal')
        self.w = tf.keras.layers.Dense(dim, use_bias=False)

        self.padding_idx = 0
        self.leakyRelu = tf.nn.leaky_relu

    def call(self, mainNodeEmbed, neighbourNodesEmbeds, numTimeSteps, users, mode):

        numNeighbours = neighbourNodesEmbeds.shape[1]

        projected_x, projected_neigh = self.w(mainNodeEmbed), self.w(neighbourNodesEmbeds)

        if mode == 'repeat':
            projected_x = tf.expand_dims(projected_x, axis=1)
            projected_x = tf.tile(projected_x, [1, numNeighbours, 1])

        concat_x_neigh = tf.concat((projected_x, projected_neigh), axis=2)
        e_ij = self.attentionDense(concat_x_neigh)
        e_ij = self.leakyRelu(e_ij)
        a_ij = tf.nn.softmax(e_ij, axis=1)

        new_x = a_ij * projected_neigh
        output_x = tf.reduce_sum(new_x, axis=1)

        return output_x


# 多头注意力层
class multiHeadAttention(tf.keras.layers.Layer):
    def __init__(self, arg, type):
        super(multiHeadAttention, self).__init__()

        self.numHeads = 1
        self.heads = {}

        for i in range(self.numHeads):
            self.heads[str(i)] = selfAttention(arg, type)

    def call(self, mainNodeEmbed, neighbourNodesEmbeds, numTimeSteps, users, mode):

        allHeads = list()

        for i in range(self.numHeads):
            output_x = self.heads[str(i)](mainNodeEmbed, neighbourNodesEmbeds, numTimeSteps, users, mode)
            output_x = tf.expand_dims(output_x, axis=1)

            allHeads.append(output_x)

        allHeadsAll = tf.stack(allHeads, axis=1)
        final_x = tf.squeeze(tf.reduce_mean(allHeadsAll, axis=1) ,axis=1)
        return final_x


# LSTM单元
class LSTMCell(tf.keras.layers.Layer):
    def __init__(self, input_size, hidden_size, bias=True):
        super(LSTMCell, self).__init__()
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.bias = bias
        self.W = tf.keras.layers.Dense(4 * hidden_size, use_bias=False)
        self.U = tf.keras.layers.Dense(4 * hidden_size, use_bias=bias)

        self.s_W = tf.keras.layers.Dense(4 * hidden_size, use_bias=False)
        self.t_W = tf.keras.layers.Dense(4 * hidden_size, use_bias=False)

    def call(self, x, hidden, spatial_per_step, temporal_per_step):

        if hidden is None:
            hidden = self._init_hidden(x)
        h_t, c_t = hidden

        previous_h_t = h_t
        previous_c_t = c_t
        x = x

        allGates_preact = self.W(x) + self.U(previous_h_t) + self.s_W(spatial_per_step) + self.t_W(
            temporal_per_step)

        input_g_ceilingIndex = self.hidden_size
        forget_g_ceilingIndex = 2 * self.hidden_size
        output_g_ceilingIndex = 3 * self.hidden_size
        cell_g_ceilingIndex = 4 * self.hidden_size

        input_g = tf.sigmoid(allGates_preact[:, :input_g_ceilingIndex])
        forget_g = tf.sigmoid(allGates_preact[:, input_g_ceilingIndex:forget_g_ceilingIndex])
        output_g = tf.sigmoid(allGates_preact[:, forget_g_ceilingIndex:output_g_ceilingIndex])
        c_t_g = tf.tanh(allGates_preact[:, output_g_ceilingIndex:cell_g_ceilingIndex])

        c_t = forget_g * previous_c_t + input_g * c_t_g
        h_t = tf.tanh(output_g * c_t)

        batchSize = x.shape[0]
        h_t_1 = tf.reshape(h_t, [batchSize, self.hidden_size])
        c_t_1 = tf.reshape(c_t, [batchSize, self.hidden_size])

        return h_t_1, c_t_1

    def _init_hidden(self, x):

        batchSize = x.shape[0]

        h = tf.zeros([batchSize, self.hidden_size])
        c = tf.zeros([batchSize, self.hidden_size])
        return h, c


# LSTM层
class ownLSTM(tf.keras.layers.Layer):

    def __init__(self, input_size, hidden_size, bias=True):
        super(ownLSTM, self).__init__()
        self.lstm_cell = LSTMCell(input_size, hidden_size, bias)
        self.hidden_size = hidden_size

    def call(self, input_, spatial, temporal, hidden=None):

        input_1 = tf.transpose(input_, perm=[1, 0, 2])
        batchSize = input_1.shape[1]

        spatial_1 = tf.transpose(spatial, [1, 0, 2])
        temporal_1 = tf.transpose(temporal, [1, 0, 2])

        outputs = list()

        for x, spatial_per_step, temporal_per_step in zip(tf.split(input_1, input_1.shape[0]), tf.split(spatial_1, spatial_1.shape[0]),
                                                          tf.split(temporal_1, temporal_1.shape[0])):

            x_1 = tf.squeeze(x, 0)
            spatial_per_step_1 = tf.squeeze(spatial_per_step, 0)
            temporal_per_step_1 = tf.squeeze(temporal_per_step, 0)


            hidden = self.lstm_cell(x_1, hidden, spatial_per_step_1, temporal_per_step_1)

            h_t, c_t = hidden

            outputs.append(tf.reshape(h_t, [1, batchSize, self.hidden_size]))

        outputs_1 = tf.squeeze(tf.stack(outputs, axis=1), axis=0)
        outputs_2 = tf.transpose(outputs_1, [1, 0, 2])
        return outputs_2, None