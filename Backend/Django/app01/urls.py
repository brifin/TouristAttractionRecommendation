from django.urls import re_path
from app01 import views

urlpatterns = [
    re_path(r'groupclass', views.get_group_class),
    re_path(r'route', views.get_pois_series),
    re_path(r'recommend', views.get_pois_recommand),

    re_path(r'historyView', views.get_view),
    re_path(r'historyStar', views.get_star),
    re_path(r'updateView', views.update_view),
    re_path(r'updateStar', views.update_star),

    re_path(r'hello', views.hello),
]