from .model import *
import pickle

def predict(model, arg, poi, user, k_des):
    route = [[[poi]]]
    route_rate = [[[1]]]
    for index in range(k_des-1):
        choice_poi = list()
        choice_rate = list()
        for ser_each in route[index]:
            for poi_each in ser_each:
                poiSeq = [poi_each]
                userSeq = [user]
                mappedGeoHash2 = arg['geohash2Index_2'][arg['poi2geohash_2'][str(poiSeq[0])]]
                mappedGeoHash3 = arg['geohash2Index_3'][arg['poi2geohash_3'][str(poiSeq[0])]]
                mappedGeoHash4 = arg['geohash2Index_4'][arg['poi2geohash_4'][str(poiSeq[0])]]
                mappedGeoHash5 = arg['geohash2Index_5'][arg['poi2geohash_5'][str(poiSeq[0])]]
                mappedGeoHash6 = arg['geohash2Index_6'][arg['poi2geohash_6'][str(poiSeq[0])]]
                input = (poiSeq, userSeq, [[mappedGeoHash2]], [[mappedGeoHash3]], [[mappedGeoHash4]],
                         [[mappedGeoHash5]], [[mappedGeoHash6]])

                pred, nextgeohashPred_2_test, nextgeohashPred_3_test, nextgeohashPred_4_test, nextgeohashPred_5_test, nextgeohashPred_6_test\
                    = model(input, training=False)
                pred = tf.reshape(tf.nn.softmax(pred, axis=2), [-1])[1:]
                sortedPreds = tf.nn.top_k(pred, 3)[1].numpy()
                sortedRates = tf.nn.top_k(pred, 3)[0].numpy()
                choice_poi.append(sortedPreds.tolist())
                choice_rate.append(sortedRates.tolist())
        route.append(choice_poi)
        route_rate.append(choice_rate)
    return route, route_rate


def get_top_route(route, rate, depth):

    def get_poi(index_list):
        poi_list = list()
        for indexs in index_list:
            poi_list.append(route[indexs[0]][indexs[1]][indexs[2]])
        return poi_list

    base1 = [[0, 0, 0], [1, 0, 0]]
    base2 = [[0, 0, 0], [1, 0, 1]]
    base3 = [[0, 0, 0], [1, 0, 2]]

    for index in range(2, depth):
        rank_list = dict()
        for i in range(3):
            temp1 = base1.copy()
            temp1.append([index, 3**(base1[index-1][0]-1)*base1[index-1][1]+base1[index-1][2], i])
            rate1 = 1
            for route_index in temp1:
                rate1 *= rate[route_index[0]][route_index[1]][route_index[2]]
            rank_list[rate1] = temp1

            temp2 = base2.copy()
            temp2.append([index, 3**(base2[index-1][0]-1)*base2[index-1][1]+base2[index-1][2], i])
            rate2 = 1
            for route_index in temp2:
                rate2 *= rate[route_index[0]][route_index[1]][route_index[2]]
            rank_list[rate2] = temp2

            temp3 = base3.copy()
            temp3.append([index, 3**(base3[index-1][0]-1)*base3[index-1][1]+base3[index-1][2], i])
            rate3 = 1
            for route_index in temp3:
                rate3 *= rate[route_index[0]][route_index[1]][route_index[2]]
            rank_list[rate3] = temp3

        for key in list(rank_list.keys()):
            pois = get_poi(rank_list[key])
            if len(pois) > len(set(pois)):
                rank_list.pop(key)

        sorted_key = sorted(rank_list.keys(), reverse=True)

        rank = 0
        base_list = list()
        top_list = set()
        while len(top_list) <= 2:
            lens = len(top_list)
            des = rank_list[sorted_key[rank]]
            per_list = set()
            for pos_index in des:
                per_list.add(route[pos_index[0]][pos_index[1]][pos_index[2]])
            top_list.add(frozenset(per_list))
            if len(top_list) > lens:
                base_list.append(des)
            rank += 1
        base1 = base_list[0]
        base2 = base_list[1]
        base3 = base_list[2]

    truth_route1 = get_poi(base1)
    truth_route2 = get_poi(base2)
    truth_route3 = get_poi(base3)
    return truth_route1, truth_route2, truth_route3


def get_near_poi(lat, lon, pos2poi):
    keys = list(pos2poi.keys())
    all_pos = [key.split('&') for key in keys]
    distance = [(lat-float(position[0]))**2+(lon-float(position[1]))**2 for position in all_pos]
    min_distance = min(distance)
    poi = pos2poi[keys[distance.index(min_distance)]]
    return poi


def get_near_user(stars, poi2user, poi):
    if len(stars) == 0:
        return poi2user[poi][0]
    else:
        star_same_user = list()
        for star in stars:
            star_same_user.append(poi2user[star])
        base = star_same_user[0]
        if len(star_same_user) == 1:
            return base[0]
            print(base)
        else:
            font_base = base
            for per_user in star_same_user:
                base = list(set(base).intersection(per_user))
                print(base)
                if len(base) == 0:
                    break
                else:
                    font_base = base
            return font_base[0]


def get_return_pos(routes, poi2pos):
    all_pos = list()
    all_poi = list()
    for route in routes:
        pos_route = list()
        poi_route = list()
        for poi in route:
            pos_route.append(poi2pos[poi])
            poi_route.append(poi)
        all_pos.append(pos_route)
        all_poi.append(poi_route)
    return all_pos, all_poi

def pre_load_nextpoi_model():
    with open('app01/MLModels/NextPOIs/arg.pickle', 'rb') as file:
        arg = pickle.load(file)

    classification = hmt_grn(arg)
    classification.load_weights('app01/MLModels/NextPOIs/trained_model')

    with open('app01/MLModels/NextPOIs/poi2pos.pickle', 'rb') as file:
        poi2pos = pickle.load(file)
    with open('app01/MLModels/NextPOIs/pos2poi.pickle', 'rb') as file:
        pos2poi = pickle.load(file)
    with open('app01/MLModels/NextPOIs/poi2user.pickle', 'rb') as file:
        poi2user = pickle.load(file)
    return arg, classification, poi2pos, pos2poi, poi2user
# pois
# [7, 6, 6]
# users
# [1, 1, 1]