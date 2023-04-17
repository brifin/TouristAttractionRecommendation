import tensorflow as tf

class UltraGCN(tf.keras.Model):
    def __init__(self, params, constraint_mat, ii_constraint_mat, ii_neighbor_mat):
        super(UltraGCN, self).__init__()
        self.user_num = params['user_num']
        self.item_num = params['item_num']
        self.embedding_dim = 64
        self.w1 = 1e-6
        self.w2 = 1
        self.w3 = 1e-6
        self.w4 = 1
        self.negative_weight = 300
        self.gamma = 1e-4
        self.lambda_ = 5e-4
        self.constraint_mat = constraint_mat
        self.ii_constraint_mat = ii_constraint_mat
        self.ii_neighbor_mat = ii_neighbor_mat

        self.user_embeds = tf.keras.layers.Embedding(self.user_num, self.embedding_dim,
                                                     embeddings_initializer=tf.keras.initializers.RandomNormal(stddev=1e-4))
        self.item_embeds = tf.keras.layers.Embedding(self.item_num, self.embedding_dim,
                                                     embeddings_initializer=tf.keras.initializers.RandomNormal(stddev=1e-4))

    def get_omegas(self, users, pos_items, neg_items):
        if self.w2 > 0:
            pos_weight = tf.multiply(tf.gather(self.constraint_mat['beta_uD'], users), tf.gather(self.constraint_mat['beta_iD'], pos_items))
            pow_weight = self.w1 + self.w2 * pos_weight
        else:
            pow_weight = self.w1 * tf.ones(len(pos_items))

        # users = (users * self.item_num).unsqueeze(0)
        if self.w4 > 0:
            neg_weight = tf.multiply(tf.repeat(tf.gather(self.constraint_mat['beta_uD'], users), neg_items.shape[1]),
                                   tf.gather(self.constraint_mat['beta_iD'], tf.reshape(neg_items, -1)))
            neg_weight = self.w3 + self.w4 * neg_weight
        else:
            neg_weight = self.w3 * tf.ones(neg_items.size(0) * neg_items.size(1))

        weight = tf.concat((pow_weight, neg_weight), axis=0)
        return weight

    def cal_loss_L(self, users, pos_items, neg_items, omega_weight):
        user_embeds = self.user_embeds(users)
        pos_embeds = self.item_embeds(pos_items)
        neg_embeds = self.item_embeds(neg_items)

        pos_scores = tf.reduce_sum((user_embeds * pos_embeds), axis=-1)  # batch_size
        user_embeds = tf.expand_dims(user_embeds, axis=1)
        neg_scores = tf.reduce_sum((user_embeds * neg_embeds), axis=-1)  # batch_size * negative_num

        neg_labels = tf.zeros(neg_scores.shape)
        neg_loss = tf.nn.weighted_cross_entropy_with_logits(neg_labels, neg_scores,
                                                      pos_weight=tf.reshape(omega_weight[len(pos_scores):], neg_scores.shape))
        neg_loss = tf.reduce_mean(neg_loss, axis=-1)

        pos_labels = tf.ones(pos_scores.shape)
        pos_loss = tf.nn.weighted_cross_entropy_with_logits(pos_labels, pos_scores,
                                                            pos_weight=omega_weight[:len(pos_scores)])

        loss = pos_loss + neg_loss * self.negative_weight

        return tf.reduce_sum(loss)

    def cal_loss_I(self, users, pos_items):
        neighbor_embeds = self.item_embeds(
            tf.gather(self.ii_neighbor_mat, pos_items))  # len(pos_items) * num_neighbors * dim
        sim_scores = \
            tf.gather(self.ii_constraint_mat, pos_items)  # len(pos_items) * num_neighbors
        user_embeds = tf.expand_dims((self.user_embeds(users)), axis=1)

        loss = -tf.cast(sim_scores, tf.float32) * \
               tf.math.log(tf.nn.sigmoid(tf.reduce_sum(user_embeds * neighbor_embeds, axis=-1)))
        return tf.reduce_sum(loss)

    def norm_loss(self):
        loss = 0.0
        for param in self.trainable_variables:
            loss += tf.reduce_sum(param ** 2)
        return loss / 2

    def call(self, users, pos_items, neg_items):
        omega_weight = self.get_omegas(users, pos_items, neg_items)

        loss = self.cal_loss_L(users, pos_items, neg_items, omega_weight)
        loss += self.gamma * self.norm_loss()
        loss += self.lambda_ * self.cal_loss_I(users, pos_items)
        return loss

    def test_foward(self, users):
        items = tf.range(self.item_num)
        user_embeds = self.user_embeds(users)
        item_embeds = self.item_embeds(items)

        return tf.matmul(user_embeds, item_embeds, transpose_b=True)
