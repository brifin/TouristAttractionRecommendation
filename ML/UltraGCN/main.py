from tools import *
from model import *
import os
import pickle

if __name__ == "__main__":
    params = {}

    params['ii_neighbor_num'] = 10
    params['max_epoch'] = 2000
    params['dataset'] = 'gowalla'
    params['learning_rate'] = 1e-4
    params['batch_size'] = 512
    params['early_stop_epoch'] = 150
    params['negative_num'] = 1500
    params['negative_weight'] = 300
    # whether to sift the pos item when doing negative sampling
    params['test_batch_size']= 2048
    params['topk']=20

    train_data, test_data, train_mat, user_num, item_num, constraint_mat = load_data()

    train_loader = tf.data.Dataset.from_tensor_slices(train_data).batch(params['batch_size']).shuffle(params['batch_size'])
    test_loader = tf.data.Dataset.from_tensor_slices(list(range(user_num))).batch(params['test_batch_size'])

    params['user_num'] = user_num
    params['item_num'] = item_num

    mask = np.zeros([user_num, item_num])
    interacted_items = [[] for _ in range(user_num)]
    for (u, i) in train_data:
        mask[u][i] = -np.inf
        interacted_items[u].append(i)

    # test user-item interaction, which is ground truth
    test_ground_truth_list = [[] for _ in range(user_num)]
    for (u, i) in test_data:
        test_ground_truth_list[u].append(i)

    ii_cons_mat_path = 'data/gowalla/_ii_constraint_mat'
    ii_neigh_mat_path = 'data/gowalla/_ii_neighbor_mat'

    if os.path.exists(ii_cons_mat_path):
        with open(ii_cons_mat_path, 'rb') as f:
            ii_constraint_mat = pickle.load(f)
        with open(ii_neigh_mat_path, 'rb') as f:
            ii_neighbor_mat = pickle.load(f)
    else:
        ii_neighbor_mat, ii_constraint_mat = get_ii_constraint_mat(train_mat, params['ii_neighbor_num'])
        with open(ii_neigh_mat_path, 'wb') as f:
            pickle.dump(ii_neighbor_mat, f)
        with open(ii_cons_mat_path, 'wb') as f:
            pickle.dump(ii_constraint_mat, f)

    ultragcn = UltraGCN(params, constraint_mat, ii_constraint_mat, ii_neighbor_mat)

    # ----------------Train--------------------
    optimizer = tf.optimizers.legacy.Adam(learning_rate=params['learning_rate'])

    best_epoch, best_recall, best_ndcg = 0, 0, 0
    early_stop_count = 0
    early_stop = False

    for epoch in range(params['max_epoch']):
        for batch, x in enumerate(train_loader):
            with tf.GradientTape() as tape:
                users, pos_items, neg_items = sampling(x, params['item_num'], params['negative_num'])
                loss = ultragcn(users, pos_items, neg_items)
            grads = tape.gradient(loss, ultragcn.trainable_variables)
            optimizer.apply_gradients(zip(grads, ultragcn.trainable_variables))

            print('epoch %d---loss: %f' % (epoch, loss), end='\r')

        need_test = True
        if epoch < 50 and epoch % 5 != 0:
            need_test = False
        if need_test:
            F1_score, Precision, Recall, NDCG = test(ultragcn, test_loader, test_ground_truth_list, mask, params['topk'],
                                                     params['user_num'])
            print("Loss = {:.5f}, F1-score: {:5f} \t Precision: {:.5f}\t Recall: {:.5f}\tNDCG: {:.5f}".format(loss, F1_score, Precision, Recall, NDCG))
            if Recall > best_recall:
                best_recall, best_ndcg, best_epoch = Recall, NDCG, epoch
                early_stop_count = 0
            else:
                early_stop_count += 1
                if early_stop_count == params['early_stop_epoch']:
                    early_stop = True

        if early_stop:
            print('Early stop is triggered at {} epochs.'.format(epoch))
            ultragcn.save_weights('ultarGCN_model')
            break