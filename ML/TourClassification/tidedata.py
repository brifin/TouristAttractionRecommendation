import pandas as pd
import tensorflow as tf
import numpy as np

sample_amount = 1500
sailings_amount = 10
tour_amount = 20

def read_data(file):
    train_data = list()
    for i in range(sample_amount):
        sample = list()
        for j in range(sailings_amount):
            sailings = list()
            for column in range(tour_amount):
                row = i*sailings_amount + j
                data = file.loc[row][column]
                data_list = data.split(',')
                sailings.append([float(data_list[0]), float(data_list[1]), float(data_list[2]), float(data_list[3])])
            sample.append(sailings)
        train_data.append(sample)
        print(i, end='\r')
    return train_data


positive_file = pd.read_csv('positive_example', header=None, sep='\t')
positive_train_data = read_data(positive_file)
positive_train_data = tf.reshape(positive_train_data, [sample_amount, sailings_amount, tour_amount, 4])
positive_train_label = tf.zeros(shape=[sample_amount])

negetive_file = pd.read_csv('negetive_example', header=None, sep='\t')
negetive_train_data = read_data(negetive_file)
negetive_train_data = tf.reshape(negetive_train_data, [sample_amount, sailings_amount, tour_amount, 4])
negetive_train_label = tf.ones(shape=[sample_amount])

train_labels = tf.concat((positive_train_label, negetive_train_label), axis=0).numpy()
train_dataset = tf.concat((positive_train_data, negetive_train_data), axis=0).numpy()

np.save("train_labels.npy", train_labels)
np.save("train_dataset.npy", train_dataset)