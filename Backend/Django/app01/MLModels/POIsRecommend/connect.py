from .model import *
import heapq
import pickle

def get_sim_user(stars, poi2user):
    star_same_user = list()
    for star in stars:
        star_same_user.append(poi2user[star])
    base = star_same_user[0]
    if len(star_same_user) == 1:
        return base[0]
    else:
        font_base = base
        for per_user in star_same_user:
            base = list(set(base).intersection(per_user))
            if len(base) == 0:
                break
            else:
                font_base = base
        return font_base[0]

def get_recommend_top(user, model, topK):
    user = tf.convert_to_tensor([user])
    rate = model.test_foward(user)
    _, K_max_item = tf.nn.top_k(rate, topK)
    return K_max_item[0].numpy()

def poi_to_remap(stars, poi_process2origin, poi_origin2remap):
    remap_stars = list()
    for star in stars:
        try:
            remap_star = poi_origin2remap[poi_process2origin[star]]
        except:
            continue
        remap_stars.append(remap_star)
    return remap_stars

def remap_to_pos(recommends, poi_remap2origin, origin2pos, origin2poi_process):
    pos_recommend = list()
    poi_recommend = list()
    for recom in recommends:
        poi = origin2poi_process[poi_remap2origin[recom]]
        pos = origin2pos[poi_remap2origin[recom]]
        pos_recommend.append([pos[0], pos[1]])
        poi_recommend.append(poi)
    return pos_recommend, poi_recommend

def preload_recommend():
    with open('app01/MLModels/POIsRecommend/params.pickle', 'rb') as f:
        params = pickle.load(f)
    with open('app01/MLModels/POIsRecommend/_ii_constraint_mat', 'rb') as f:
        ii_constraint_mat = pickle.load(f)
    with open('app01/MLModels/POIsRecommend/_ii_neighbor_mat', 'rb') as f:
        ii_neighbor_mat = pickle.load(f)
    with open('app01/MLModels/POIsRecommend/constraint_mat.pickle', 'rb') as f:
        constraint_mat = pickle.load(f)

    ultragcn = UltraGCN(params, constraint_mat, ii_constraint_mat, ii_neighbor_mat)
    ultragcn.load_weights('app01/MLModels/POIsRecommend/ultarGCN_model')

    with open('app01/MLModels/POIsRecommend/index2user.pickle', 'rb') as file:
        index2user = pickle.load(file)
    with open('app01/MLModels/POIsRecommend/origin2remap.pickle', 'rb') as file:
        poi_origin2remap = pickle.load(file)
    with open('app01/MLModels/POIsRecommend/remap2origin.pickle', 'rb') as file:
        poi_remap2origin = pickle.load(file)
    with open('app01/MLModels/POIsRecommend/process2origin.pickle', 'rb') as file:
        poi_process2origin = pickle.load(file)
    with open('app01/MLModels/POIsRecommend/origin2pos.pickle', 'rb') as file:
        origin2pos = pickle.load(file)

    return index2user, ultragcn, poi_origin2remap, poi_remap2origin, poi_process2origin, origin2pos

