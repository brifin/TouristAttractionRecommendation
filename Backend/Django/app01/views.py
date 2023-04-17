from .MLModels.BiClass.connect import *
from .MLModels.NextPOIs.connect import *
from .MLModels.POIsRecommend.connect import *

from .models import *

from django.shortcuts import HttpResponse
import json

biclass_model = pre_load_biclass()
arg, nextpoi_model, poi2pos, pos2poi, poi2user = pre_load_nextpoi_model()
index2user, recommend_model, poi_origin2remap, poi_remap2origin, poi_process2origin, origin2pos = preload_recommend()
origin2poi_process = dict()
for k, v in poi_process2origin.items():
    origin2poi_process[v] = k


def hello(request):
    return HttpResponse("Hello world ! ")


def get_group_class(request):
    data = json.loads(request.body)
    msg = data['schedule']
    input = read_data(msg)
    prob = biclass_model(input)
    pred = prob.numpy().tolist()[0][0] > prob.numpy().tolist()[0][1]
    result = {'isScatteredGroups': pred}
    return HttpResponse(json.dumps(result))


def get_pois_series(request):
    data = json.loads(request.body)
    depth = 7
    lat = data['latitude']
    lon = data['longitude']
    stars = data['stars']
    poi = get_near_poi(lat, lon, pos2poi)
    user = get_near_user(stars, poi2user, poi)
    route, route_rate = predict(nextpoi_model, arg, poi, user, depth)
    truth_route1, truth_route2, truth_route3 = get_top_route(route, route_rate, depth)
    all_pos, all_poi = get_return_pos([truth_route1, truth_route2, truth_route3], poi2pos)
    return HttpResponse(json.dumps({'allRoute': all_pos, 'map_poi': all_poi}))


def get_most_stars(topK):
    ordered_stars = POIStar.objects.order_by('-thumbsup')
    i = 0
    pos_recommend = list()
    poi_recommend = list()
    for star in ordered_stars:
        pos_recommend.append([star.latitude, star.longitude])
        poi_recommend.append(star.poi)
        print(star)
        i += 1
        if i == topK:
            break
    return pos_recommend, poi_recommend


def get_pois_recommand(request):
    topK = 30
    data = json.loads(request.body)
    stars = data['stars']
    remap_stars = poi_to_remap(stars, poi_process2origin, poi_origin2remap)
    if len(remap_stars) == 0:
        pos_recommend, poi_recommend = get_most_stars(topK)
    else:
        user = get_sim_user(remap_stars, index2user)
        recommends = get_recommend_top(user, recommend_model, topK)
        pos_recommend, poi_recommend = remap_to_pos(recommends, poi_remap2origin, origin2pos, origin2poi_process)
    result = {'recommends': pos_recommend, 'map_poi': poi_recommend}
    return HttpResponse(json.dumps(result))


def update_view(request):
    data = json.loads(request.body)
    user_view = UserView.objects.filter(Q(nickname=data['nickname']) & Q(poi=data['poi']))
    if user_view.exists() is False:
        user_view = UserView(
            nickname=data['nickname'],
            poi=data['poi'],
            latitude=data['lat'],
            longitude=data['lon'],
            timestamp=data['timestamp']
        )
        user_view.save()
    else:
        user_view.update(timestamp=data['timestamp'])
    return HttpResponse(json.dumps({'success': True}))


def update_star(request):
    data = json.loads(request.body)
    pois = POIStar.objects.filter(poi=data['poi'])
    if data['star']:
        user_star = UserStar(
            nickname=data['nickname'],
            poi=data['poi'],
            latitude=data['lat'],
            longitude=data['lon'],
            timestamp=data['timestamp']
        )
        user_star.save()

        if pois.exists() is False:
            poi_star = POIStar(
                poi=data['poi'],
                latitude=data['lat'],
                longitude=data['lon'],
                thumbsup=1
            )
            poi_star.save()
        else:
            pois.update(thumbsup=pois[0].thumbsup + 1)
    else:
        user_star = UserStar.objects.filter(Q(nickname=data['nickname']) & Q(poi=data['poi']))
        user_star[0].delete()

        pois.update(thumbsup=pois[0].thumbsup - 1)

    return HttpResponse(json.dumps({'success': True}))


def get_view(request):
    data = json.loads(request.body)
    views = UserView.objects.filter(nickname=data['nickname'])
    result = list()
    for view in views:
        result.append({
            'poi': view.poi,
            'latitude': view.latitude,
            'longitude': view.longitude,
            'timestamp': view.timestamp
        })
    return HttpResponse(json.dumps(result))


def get_star(request):
    data = json.loads(request.body)
    views = UserStar.objects.filter(nickname=data['nickname'])
    result = list()
    for view in views:
        result.append({
            'poi': view.poi,
            'latitude': view.latitude,
            'longitude': view.longitude,
            'timestamp': view.timestamp
        })
    return HttpResponse(json.dumps(result))
