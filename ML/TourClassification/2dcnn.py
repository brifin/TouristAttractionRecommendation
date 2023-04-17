import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split

train_data = np.load('train_dataset.npy')
train_label = np.load('train_labels.npy')

x_train, x_test, y_train, y_test = train_test_split(train_data, train_label, test_size=0.2)

def init_model():
    model = tf.keras.Sequential([
        tf.keras.layers.Conv2D(filters=8, kernel_size=7, activation='relu'),
        tf.keras.layers.MaxPool2D(pool_size=3),
        tf.keras.layers.Flatten(),
        tf.keras.layers.Dense(32, 'relu'),
        tf.keras.layers.Dense(16, 'relu'),
        tf.keras.layers.Dense(2, 'softmax')
    ])
    return model

model = init_model()
# model.build(input_shape=(None, 10, 20, 4))
# model.summary()
model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])
history = model.fit(x_train, y_train, validation_data=(x_test, y_test), validation_freq=1, epochs=20, batch_size=32)
# model.evaluate(x_test, y_test)
#
# # model.save('biclass')