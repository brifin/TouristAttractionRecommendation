import networkx as nx
from model import *
from utils import *
from tqdm import tqdm
import pickle
import sys

if __name__ == '__main__':
    arg = {}

    # 定义超参数
    arg['epoch'] = 5
    arg['beamSize'] = 100
    arg['embedding_dim'] = 1024
    arg['userEmbed_dim'] = 1024
    arg['hidden_dim'] = 1024
    arg['classification_learning_rate'] = 0.0001
    arg['classification_batch'] = 32
    arg['dropout'] = 0.9

    # 引入预处理数据
    arg['temporalGraph'] = nx.read_edgelist('./processedFiles/gowalla_temporal.edgelist',
                                            nodetype=int,
                                            create_using=nx.Graph())

    arg['spatialGraph'] = nx.read_edgelist(
        './processedFiles/gowalla_spatial.edgelist', nodetype=int,
        create_using=nx.Graph())

    spatialGraphDict = dict()
    for i in range(len(arg['spatialGraph'])):
        try:
            sample = [j for j in arg['spatialGraph'][i]]
            spatialGraphDict[str(i)] = sample
        except:
            spatialGraphDict[str(i)] = [i]

    temporalGraphDict = dict()
    for i in range(len(arg['temporalGraph'])):
        try:
            sample = [j for j in arg['temporalGraph'][i]]
            temporalGraphDict[str(i)] = sample
        except:
            temporalGraphDict[str(i)] = [i]

    arg['temporalGraph'] = temporalGraphDict
    arg['spatialGraph'] = spatialGraphDict

    with open('./processedFiles/gowalla_userCount.pickle', 'rb') as handle:
        arg['numUser'] = pickle.load(handle)

    # index to geohash: {1: 'dr5ru2', 2: 'dr5x1n'
    # poi to geohash: {'dr5ru2': [1, 473, 1415], 'dr5x1n': [2, 1202],

    for eachGeoHashPrecision in [6, 5, 4, 3, 2]:
        with open('./processedFiles/gowalla_poi2geohash_' + str(eachGeoHashPrecision) + '.pickle', 'rb') as handle:
            arg['poi2geohash_' + str(eachGeoHashPrecision)] = pickle.load(handle)

            arg['poi2geohash_' + str(eachGeoHashPrecision)] = {str(k): v
                                    for k, v in arg['poi2geohash_' + str(eachGeoHashPrecision)].items()}
        with open('./processedFiles/gowalla_geohash2poi_' + str(eachGeoHashPrecision) + '.pickle', 'rb') as handle:
            arg['geohash2poi_' + str(eachGeoHashPrecision)] = pickle.load(handle)

        with open('./processedFiles/gowalla_geohash2Index_' + str(eachGeoHashPrecision) + '.pickle', 'rb') as handle:
            arg['geohash2Index_' + str(eachGeoHashPrecision)] = pickle.load(handle)

        arg['index2geoHash_' + str(eachGeoHashPrecision)] = \
            {str(v): k for k, v in arg['geohash2Index_' + str(eachGeoHashPrecision)].items()}

    with open('./processedFiles/gowalla_beamSearchHashDict.pickle', 'rb') as handle:
        arg['beamSearchHashDict'] = pickle.load(handle)

    print("1. train line51 done")
    # 准备训练数据
    data = make_batches(arg['numUser'], arg)

    print("2. train line55 done")
    # data:list 11864
    classification_dataloader = tf.data.Dataset.from_tensor_slices(data)\
        .shuffle(12000).batch(arg['classification_batch']).prefetch(tf.data.AUTOTUNE)

    # 创建模型
    # classification = tf.cast(hmt_grn(arg), tf.float32)
    classification = hmt_grn(arg)
    print("3. train line62 done")
    # 创建优化器
    classification_optim = tf.keras.optimizers.legacy.Adam(learning_rate=arg['classification_learning_rate'])

    # 定义损失函数
    criterion = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)
    nextGeoHashCriterion_2 = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)
    nextGeoHashCriterion_3 = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)
    nextGeoHashCriterion_4 = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)
    nextGeoHashCriterion_5 = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)
    nextGeoHashCriterion_6 = tf.losses.SparseCategoricalCrossentropy(from_logits=True, ignore_class=0)

    # 开始训练
    for epoch in range(1, arg['epoch'] + 1):

        avgLossDict = {}

        print('Epoch: ' + str(epoch))

        avgLossDict['Next POI Classification'] = []

        #tqdm是一个进度条函数
        classification_pbar = tqdm(classification_dataloader)
        classification_pbar.set_description("[gowalla_Classification-Epoch {}]".format(epoch))

        # for (x, user, y) in classification_pbar:
        for ele in classification_pbar:
            x, user, y = tf.split(ele, num_or_size_splits=3, axis=1)
            x = tf.squeeze(x, axis=1)
            user = tf.squeeze(user, axis=1)
            y = tf.squeeze(y, axis=1)

            actualBatchSize = x.shape[0]

            batchLoss = 0

            x_geoHash2 = tf.cast([], tf.int64)
            x_geoHash3 = tf.cast([], tf.int64)
            x_geoHash4 = tf.cast([], tf.int64)
            x_geoHash5 = tf.cast([], tf.int64)
            x_geoHash6 = tf.cast([], tf.int64)

            for eachBatch in range(x.shape[0]):
                sample = x[eachBatch].numpy().tolist()

                # 初始化赋值
                if x_geoHash2.shape == 0:
                    mappedGeohash = [arg['geohash2Index' + '_2'][arg['poi2geohash' + '_2'][str(i)]] for i in sample]
                    x_geoHash2 = tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_3'][arg['poi2geohash' + '_3'][str(i)]] for i in sample]
                    x_geoHash3 = tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_4'][arg['poi2geohash' + '_4'][str(i)]] for i in sample]
                    x_geoHash4 = tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_5'][arg['poi2geohash' + '_5'][str(i)]] for i in sample]
                    x_geoHash5 = tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_6'][arg['poi2geohash' + '_6'][str(i)]] for i in sample]
                    x_geoHash6 = tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)
                else:
                    mappedGeohash = [arg['geohash2Index' + '_2'][arg['poi2geohash' + '_2'][str(i)]] for i in sample]
                    x_geoHash2 = tf.concat((x_geoHash2, tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)),
                                           axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_3'][arg['poi2geohash' + '_3'][str(i)]] for i in sample]
                    x_geoHash3 = tf.concat((x_geoHash3, tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)),
                                           axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_4'][arg['poi2geohash' + '_4'][str(i)]] for i in sample]
                    x_geoHash4 = tf.concat((x_geoHash4, tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)),
                                           axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_5'][arg['poi2geohash' + '_5'][str(i)]] for i in sample]
                    x_geoHash5 = tf.concat((x_geoHash5, tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)),
                                           axis=0)

                    mappedGeohash = [arg['geohash2Index' + '_6'][arg['poi2geohash' + '_6'][str(i)]] for i in sample]
                    x_geoHash6 = tf.concat((x_geoHash6, tf.expand_dims(tf.cast(mappedGeohash, tf.int64), axis=0)),
                                           axis=0)

            inputs = (x, user, y, x_geoHash2, x_geoHash3, x_geoHash4, x_geoHash5, x_geoHash6)

            truth = tf.cast(y.numpy(), tf.float32)

            # map truth to geohash
            truthDict = {}

            for eachGeoHashPrecision in [6, 5, 4, 3, 2]:
                name = 'nextGeoHashTruth' + '_' + str(eachGeoHashPrecision)
                behind = '_' + str(eachGeoHashPrecision)
                truthDict[name] = tf.cast([], tf.float32)

                for eachBatch in range(truth.shape[0]):
                    sample = truth[eachBatch].numpy().tolist()
                    mappedNextGeohashTruth = [arg['geohash2Index' + behind][arg['poi2geohash' + behind][str(int(i))]] for i in
                                              sample]

                    if truthDict[name].shape == 0:
                        truthDict[name] = tf.expand_dims(tf.cast(mappedNextGeohashTruth, tf.float32), axis=0)
                    else:
                        truthDict[name] = tf.concat((truthDict[name],
                                                     tf.expand_dims(tf.cast(mappedNextGeohashTruth, tf.float32),
                                                                    axis=0)), axis=0)

            with tf.GradientTape() as tape:
                logSoftmaxScores, nextgeohashPred_2, nextgeohashPred_3, nextgeohashPred_4, nextgeohashPred_5, nextgeohashPred_6 = \
                    classification(inputs, training=True)
                # 数量级e-1

                class_size = logSoftmaxScores.shape[2]

                classification_loss = criterion(tf.reshape(truth, [-1]), tf.reshape(logSoftmaxScores, [-1, class_size]))
                nextGeoHash_loss_2 = nextGeoHashCriterion_2(tf.reshape(truthDict['nextGeoHashTruth_2'], [-1]),
                                                            tf.reshape(nextgeohashPred_2, [-1, len(arg['geohash2Index_2'])]))
                nextGeoHash_loss_3 = nextGeoHashCriterion_3(tf.reshape(truthDict['nextGeoHashTruth_3'], [-1]),
                                                            tf.reshape(nextgeohashPred_3, [-1, len(arg['geohash2Index_3'])]))
                nextGeoHash_loss_4 = nextGeoHashCriterion_4(tf.reshape(truthDict['nextGeoHashTruth_4'], [-1]),
                                                            tf.reshape(nextgeohashPred_4, [-1, len(arg['geohash2Index_4'])]))
                nextGeoHash_loss_5 = nextGeoHashCriterion_5(tf.reshape(truthDict['nextGeoHashTruth_5'], [-1]),
                                                            tf.reshape(nextgeohashPred_5, [-1, len(arg['geohash2Index_5'])]))
                nextGeoHash_loss_6 = nextGeoHashCriterion_6(tf.reshape(truthDict['nextGeoHashTruth_6'], [-1]),
                                                            tf.reshape(nextgeohashPred_6, [-1, len(arg['geohash2Index_6'])]))

                batchLoss = (classification_loss + nextGeoHash_loss_2 + nextGeoHash_loss_3
                             + nextGeoHash_loss_4 + nextGeoHash_loss_5 + nextGeoHash_loss_6) / 6 / actualBatchSize


            # classification.summary()
            # 计算梯度迭代
            # print(batchLoss)
            grad = tape.gradient(batchLoss, classification.trainable_variables)
            classification_optim.apply_gradients(zip(grad, classification.trainable_variables))

            classification_pbar.set_postfix(loss=classification_loss.numpy()/actualBatchSize)
            avgLossDict['Next POI Classification'].append(classification_loss.numpy()/actualBatchSize)
            break

        # 打印每一个epoch的损失
        avgLossDict['Next POI Classification'] = np.average(avgLossDict['Next POI Classification'])
        print('Next POI Classification Avg Loss: ' + str(avgLossDict['Next POI Classification']))

        sys.stdout.flush()

        # 每20次进行一次验证
        # if epoch % 20 == 0:
        #
        #     print('Epoch ' + str(epoch) + ' Evaluation Start!')
        #
        model = classification

        arg['novelEval'] = False
        evaluate(model, arg)
        #
        #     arg['novelEval'] = True
        #     evaluate(model, arg)
        #
        # sys.stdout.flush()
    #
    # print(classification.trainable_variables)
    # classification.save_weights('trained_model')