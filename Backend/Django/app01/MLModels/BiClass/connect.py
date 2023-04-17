import tensorflow as tf

def read_data(msg):
    cur_data = list()
    per_loca = msg.split('    ')
    if len(per_loca) != 20:
        per_loca = msg.split('\t')
    for loca in per_loca:
        each_info = loca.split(',')
        cur_data.append([float(each_info[0]), float(each_info[1]), float(each_info[2]), float(each_info[3])])
    test_data = [cur_data]
    while len(test_data) < 10:
        test_data.append(cur_data)
    test_data = tf.reshape(test_data, [1,10,20,4])
    return test_data

def pre_load_biclass():
    biclass_model = tf.keras.models.load_model('app01/MLModels/BiClass/biclass')
    return biclass_model

# False: 精品团 True: 散团
