import pickle
import numpy as np
from tqdm import tqdm
import tensorflow as tf

def make_batches(numUsers, arg):
    with open('./processedFiles/gowalla_poiCount.pickle', 'rb') as handle:
        n_poi = pickle.load(handle)

    arg['vocab_size'] = n_poi
    arg['num_classes'] = n_poi
    arg['numUsers'] = numUsers

    with open('./processedFiles/gowalla_train.pkl', 'rb') as f:
        pois_seq, delta_t_seq, delta_d_seq = pickle.load(f)

    arg['user2TrainSet'] = {}
    for userID, sample in enumerate(pois_seq, start=1):
        arg['user2TrainSet'][userID] = sample

    arg['user2TrainLength'] = {}
    for userID, sample in enumerate(pois_seq, start=1):
        arg['user2TrainLength'][userID] = len(sample)

    x_train = np.array([seq[:-1] for seq in pois_seq])
    y_train = np.array([seq[1:] for seq in pois_seq])
    arg['max_length'] = np.max([len(i) for i in x_train])

    with open('./processedFiles/gowalla_usersData.pickle', 'rb') as handle:
        userData = pickle.load(handle)

    trainUser, testUser = userData

    trainUser = [seq[:-1] for seq in trainUser]
    trainUser = np.array(trainUser)

    # 未满足最大长度的序列右侧添加0
    def zeroPaddingToRight(data, paddingIndex):
        max_length = arg['max_length']
        paddedOutput = [i + [paddingIndex] * (max_length - len(i)) if
                            len(i) < arg['max_length'] else i for i in data]
        return paddedOutput

    padding_index = 0

    trainUser = np.array(zeroPaddingToRight(trainUser, padding_index))
    x_train = np.array(zeroPaddingToRight(x_train, padding_index))
    y_train = np.array(zeroPaddingToRight(y_train, padding_index))

    data = [(x_train[i], trainUser[i], y_train[i]) for i in range(x_train.shape[0])]
    return data


def evaluate(model, arg):

    with open('./processedFiles/gowalla_test.pkl', 'rb') as f:
        test_pois_seq, test_delta_t_seq, test_delta_d_seq = pickle.load(f)

    with open('./processedFiles/gowalla_train.pkl', 'rb') as f:
        train_pois_seq, train_delta_t_seq, train_delta_d_seq = pickle.load(f)

    with open('./processedFiles/gowalla_usersData.pickle', 'rb') as handle:
        userData = pickle.load(handle)

    trainUser, testUser = userData

    acc_K = [1, 5, 10, 20]

    result = {}
    totalMAP = []

    for K in acc_K:
        result[K] = 0

    totalTestInstances = 0

    for index in tqdm(range(len(test_pois_seq))):

        userAP = []
        test_set_poi_seq = [i for i in test_pois_seq[index]]
        test_set_user_seq = [i for i in testUser[index]]

        totalCheckins = 0
        hits = dict()  #
        for K in acc_K:  # initialize the dict for each K.
            hits[K] = 0

        poi_test = test_set_poi_seq[:-1]
        user_test = test_set_user_seq[:-1]
        target_test = test_set_poi_seq[1:]

        for i in range(len(poi_test)):

            poiSeq = [poi_test[i]]
            userSeq = [user_test[i]]

            target_item = target_test[i]

            if arg['novelEval']:
                if target_item in train_pois_seq[index]: # we ignore the evaluation of this sample and continue to next.
                    continue

            totalCheckins += 1

            mappedGeoHash2 = arg['geohash2Index_2'][arg['poi2geohash_2'][str(poiSeq[0])]]
            mappedGeoHash3 = arg['geohash2Index_3'][arg['poi2geohash_3'][str(poiSeq[0])]]
            mappedGeoHash4 = arg['geohash2Index_4'][arg['poi2geohash_4'][str(poiSeq[0])]]
            mappedGeoHash5 = arg['geohash2Index_5'][arg['poi2geohash_5'][str(poiSeq[0])]]
            mappedGeoHash6 = arg['geohash2Index_6'][arg['poi2geohash_6'][str(poiSeq[0])]]

            input = (poiSeq, userSeq, [[mappedGeoHash2]], [[mappedGeoHash3]], [[mappedGeoHash4]],
                     [[mappedGeoHash5]], [[mappedGeoHash6]])

            userID = index + 1
            assert userID == userSeq[0]  #  double check


            pred, nextgeohashPred_2_test, nextgeohashPred_3_test, nextgeohashPred_4_test, nextgeohashPred_5_test, nextgeohashPred_6_test = model(
                input, training=False)
            pred = tf.nn.softmax(pred, axis=2)
            nextgeohashPred_2_test = tf.nn.softmax(nextgeohashPred_2_test, axis=2)
            nextgeohashPred_3_test = tf.nn.softmax(nextgeohashPred_3_test, axis=2)
            nextgeohashPred_4_test = tf.nn.softmax(nextgeohashPred_4_test, axis=2)
            nextgeohashPred_5_test = tf.nn.softmax(nextgeohashPred_5_test, axis=2)
            nextgeohashPred_6_test = tf.nn.softmax(nextgeohashPred_6_test, axis=2)

            pred = tf.reshape(pred, [-1])
            pred = pred[1:]
            target_item -= 1
            sortedPreds = tf.nn.top_k(pred, len(pred))[1].numpy()

            temptHistoryPOIs = [i - 1 for i in list(set(train_pois_seq[index]))]

            # Hierarchical Beam Search
            if sortedPreds[0] not in temptHistoryPOIs or arg['novelEval']:  # unvisited

                allSequenceDict = {}
                sequencesDict = {}

                for iterationIndex in [2, 3, 4, 5, 6]:
                    all_candidates = []
                    if iterationIndex == 2:
                        row = nextgeohashPred_2_test[0][0]
                        try:
                            topBeam = tf.nn.top_k(row, arg['beamSize'])
                        except:
                            topBeam = tf.nn.top_k(row, len(row))
                        topBeam_indices = topBeam.indices.numpy()
                        topBeam_Prob = topBeam.values.numpy()
                        wholeSeqList = topBeam_indices
                    else:
                        topBeam_indices = [i[0][-1:][0] for i in sequencesDict[iterationIndex - 1]]
                        topBeam_Prob = [i[1] for i in sequencesDict[iterationIndex - 1]]
                        wholeSeqList = [i[0] for i in sequencesDict[iterationIndex - 1]]

                    if 0 in topBeam_indices: #remove 0 padding
                        mask = topBeam_indices != 0
                        topBeam_indices = topBeam_indices[mask]

                    # get sub-nodes
                    for eachTopK, prob, pastSeqList in zip(topBeam_indices, topBeam_Prob, wholeSeqList):
                        try:
                            beforeHash = arg['index2geoHash' + '_' + str(iterationIndex)][str(eachTopK)]
                        except:
                            continue
                        if iterationIndex == 6:
                            mappedGeoHash_last = arg['index2geoHash_6'][str(eachTopK)]
                            geoHashPOIs_last = arg['geohash2poi_6'][mappedGeoHash_last]
                            subNodes2Index = [i - 1 for i in geoHashPOIs_last]
                        else:
                            currentPrecisionRelation = str(iterationIndex) + '_' + str(iterationIndex + 1)
                            subNodes = arg['beamSearchHashDict'][currentPrecisionRelation][beforeHash]
                            # map sub-nodes to index
                            subNodes2Index = [arg['geohash2Index' + '_' + str(iterationIndex + 1)][i] for i in
                                              subNodes]
                        # get their probabilities from prediction
                        if iterationIndex == 2:
                            geohashPredChoice = nextgeohashPred_3_test
                        elif iterationIndex == 3:
                            geohashPredChoice = nextgeohashPred_4_test
                        elif iterationIndex == 4:
                            geohashPredChoice = nextgeohashPred_5_test
                        elif iterationIndex == 5:
                            geohashPredChoice = nextgeohashPred_6_test
                        elif iterationIndex == 6:
                            geohashPredChoice = pred

                        if type(pastSeqList) is int:
                            pastSeqList = [pastSeqList]

                        geohashPredChoice = tf.reshape(geohashPredChoice, [-1]).numpy()
                        subNodes_Probs = [geohashPredChoice[index] for index in subNodes2Index]

                        for eachSubNodeIndex in range(len(subNodes2Index)):
                            # eachSubNodeIndex
                            candidate = [pastSeqList + [subNodes2Index[eachSubNodeIndex]],
                                         prob + np.log(subNodes_Probs[eachSubNodeIndex])]
                            all_candidates.append(candidate)

                    ordered = sorted(all_candidates, key=lambda tup: tup[1], reverse=True)
                    output = ordered[:arg['beamSize']]
                    allSequenceDict[iterationIndex] = ordered
                    sequencesDict[iterationIndex] = output


                allCandidate_lastPOIs = [i[0][-1:][0] for i in ordered]

                remaining = set(range(pred.shape[0])) - set(allCandidate_lastPOIs)
                sortedPreds = list(allCandidate_lastPOIs) + list(remaining)

            # =====================Hierarchical Beam Search====================

            # for novel eval, to remain only novel recommendations, we remove the user's visited POI from the distribution or ranked list
            if arg['novelEval']:
                historyPOIs = list(set(train_pois_seq[index]))
                historyPOIs = [i - 1 for i in historyPOIs]
                sortedPreds = [i for i in sortedPreds if i not in historyPOIs]

            truthIndex = sortedPreds[target_item] + 1

            averagePrecision = 1 / truthIndex

            userAP.append(averagePrecision)

            sorted_indexs = {}
            for K in acc_K:
                sorted_indexs[K] = sortedPreds[:K]

            # Check if ground truth in top K for each acc@K
            for K in acc_K:
                if target_item in sorted_indexs[K]:
                    hits[K] = hits[K] + 1

        totalTestInstances += totalCheckins

        for K in acc_K:
            result[K] += hits[K]

        userMAP = userAP

        totalMAP = totalMAP + userMAP

    for K in acc_K:
        result[K] /= totalTestInstances

    print(str(result[1]) + ',' + str(result[5]) + ',' + str(result[10]) + ',' + str(result[20]) + ',' + str(
        np.average(totalMAP)), end='')

