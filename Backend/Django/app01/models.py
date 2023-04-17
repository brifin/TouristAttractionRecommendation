from django.db import models
from django.db.models import Q

# 用户浏览
class UserView(models.Model):
    nickname = models.CharField(max_length=40, verbose_name='昵称')
    poi = models.IntegerField(verbose_name='POI序号')
    latitude = models.FloatField(verbose_name='纬度')
    longitude = models.FloatField(verbose_name='经度')
    timestamp = models.CharField(max_length=20,verbose_name='时间')

    class Meta:
        db_table = 'user_views'  # 指明数据库表名
        verbose_name = '用户浏览'  # 在admin站点中显示的名称
        verbose_name_plural = verbose_name  # 显示的复数名称


# 用户点赞
class UserStar(models.Model):
    nickname = models.CharField(max_length=40, verbose_name='昵称')
    poi = models.IntegerField(verbose_name='POI序号')
    latitude = models.FloatField(verbose_name='纬度')
    longitude = models.FloatField(verbose_name='经度')
    timestamp = models.CharField(max_length=20,verbose_name='时间')

    class Meta:
        db_table = 'user_stars'  # 指明数据库表名
        verbose_name = '用户点赞'  # 在admin站点中显示的名称
        verbose_name_plural = verbose_name  # 显示的复数名称

# 地点被点赞
class POIStar(models.Model):
    poi = models.IntegerField(verbose_name='POI序号')
    latitude = models.FloatField(verbose_name='纬度')
    longitude = models.FloatField(verbose_name='经度')
    thumbsup = models.IntegerField(verbose_name='点赞数')

    class Meta:
        db_table = 'poi_stars'  # 指明数据库表名
        verbose_name = '地点点赞'  # 在admin站点中显示的名称
        verbose_name_plural = verbose_name  # 显示的复数名称
