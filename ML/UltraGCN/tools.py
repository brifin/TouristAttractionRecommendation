import numpy as np
import scipy as sp
import tensorflow as tf

def load_data():
    train_file = 'data/gowalla/train.txt'
    test_file = 'data/gowalla/test.txt'

    trainUniqueUsers, trainItem, trainUser = [], [], []
    testUniqueUsers, testItem, testUser = [], [], []
    n_user, m_item = 0, 0
    trainDataSize, testDataSize = 0, 0
    with open(train_file, 'r') as f:
        for l in f.readlines():
            if len(l) > 0:
                l = l.strip('\n').split(' ')
                items = [int(i) for i in l[1:]]
                uid = int(l[0])
                trainUniqueUsers.append(uid)
                trainUser.extend([uid] * len(items))
                trainItem.extend(items)
                m_item = max(m_item, max(items))
                n_user = max(n_user, uid)
                trainDataSize += len(items)
    trainUniqueUsers = np.array(trainUniqueUsers)
    trainUser = np.array(trainUser)
    trainItem = np.array(trainItem)

    with open(test_file) as f:
        for l in f.readlines():
            if len(l) > 0:
                l = l.strip('\n').split(' ')
                try:
                    items = [int(i) for i in l[1:]]
                except:
                    items = []
                uid = int(l[0])
                testUniqueUsers.append(uid)
                testUser.extend([uid] * len(items))
                testItem.extend(items)
                try:
                    m_item = max(m_item, max(items))
                except:
                    m_item = m_item
                n_user = max(n_user, uid)
                testDataSize += len(items)

    train_data = []
    test_data = []

    n_user += 1
    m_item += 1

    for i in range(len(trainUser)):
        train_data.append([trainUser[i], trainItem[i]])
    for i in range(len(testUser)):
        test_data.append([testUser[i], testItem[i]])
    train_mat = sp.sparse.dok_matrix((n_user, m_item), dtype=np.float32)

    for x in train_data:
        train_mat[x[0], x[1]] = 1.0

    items_D = np.sum(train_mat, axis = 0).reshape(-1)
    users_D = np.sum(train_mat, axis = 1).reshape(-1)

    beta_uD = (np.sqrt(users_D + 1) / users_D).reshape(-1, 1)
    beta_iD = (1 / np.sqrt(items_D + 1)).reshape(1, -1)

    constraint_mat = {"beta_uD": tf.reshape(beta_uD, -1),
                      "beta_iD": tf.reshape(beta_iD, -1)}

    return train_data, test_data, train_mat, n_user, m_item, constraint_mat


def get_ii_constraint_mat(train_mat, num_neighbors, ii_diagonal_zero = False):
    A = train_mat.T.dot(train_mat)	# I * I
    n_items = A.shape[0]
    res_mat = np.zeros([n_items, num_neighbors])
    res_sim_mat = np.zeros([n_items, num_neighbors])
    if ii_diagonal_zero:
        A[range(n_items), range(n_items)] = 0
    items_D = np.sum(A, axis = 0).reshape(-1)
    users_D = np.sum(A, axis = 1).reshape(-1)

    beta_uD = (np.sqrt(users_D + 1) / users_D).reshape(-1, 1)
    beta_iD = (1 / np.sqrt(items_D + 1)).reshape(1, -1)
    all_ii_constraint_mat = tf.convert_to_tensor(beta_uD.dot(beta_iD))
    for i in range(n_items):
        row = all_ii_constraint_mat[i] * tf.convert_to_tensor(A.getrow(i).toarray()[0])
        row_sims, row_idxs = tf.nn.top_k(row, num_neighbors)
        res_mat[i] = row_idxs
        res_sim_mat[i] = row_sims
    return tf.convert_to_tensor(res_mat), tf.convert_to_tensor(res_sim_mat)

def sampling(pos_train_data, item_num, neg_ratio):
    neg_candidates = np.arange(item_num)
    neg_items = np.random.choice(neg_candidates, [pos_train_data.shape[0], neg_ratio], replace=True)
    neg_items = tf.convert_to_tensor(neg_items)

    return pos_train_data[:, 0], pos_train_data[:, 1], neg_items  # users, pos_items, neg_items


def test(model, test_loader, test_ground_truth_list, mask, topk, n_user):
    rating_list = []
    groundTrue_list = []

    for idx, batch_users in enumerate(test_loader):
        rating = model.test_foward(batch_users)
        rating += mask[batch_users]

        _, rating_K = tf.nn.top_k(rating, k=topk)

        rating_list.append(rating_K)
        groundTrue_list.append([test_ground_truth_list[u] for u in batch_users])

    X = zip(rating_list, groundTrue_list)
    Recall, Precision, NDCG = 0, 0, 0

    for i, x in enumerate(X):
        precision, recall, ndcg = test_one_batch(x, topk)
        Recall += recall
        Precision += precision
        NDCG += ndcg

    Precision /= n_user
    Recall /= n_user
    NDCG /= n_user
    F1_score = 2 * (Precision * Recall) / (Precision + Recall)

    return F1_score, Precision, Recall, NDCG

def test_one_batch(X, k):
    sorted_items = X[0].numpy()
    groundTrue = X[1]

    r = []
    for i in range(len(groundTrue)):
        groundTrues = groundTrue[i]
        predictTopK = sorted_items[i]
        pred = list(map(lambda x: x in groundTrues, predictTopK))
        pred = np.array(pred).astype("float")
        r.append(pred)
    r = np.array(r).astype('float')

    right_pred = r[:, :k].sum(1)
    precis_n = k

    recall_n = np.array([len(groundTrue[i]) for i in range(len(groundTrue))])
    recall_n = np.where(recall_n != 0, recall_n, 1)
    recall = np.sum(right_pred / recall_n)
    precis = np.sum(right_pred) / precis_n
    ret = {'recall': recall, 'precision': precis}

    pred_data = r[:, :k]

    test_matrix = np.zeros([len(pred_data), k])
    for i, items in enumerate(groundTrue):
        length = k if k <= len(items) else len(items)
        test_matrix[i, :length] = 1
    max_r = test_matrix
    idcg = np.sum(max_r * 1. / np.log2(np.arange(2, k + 2)), axis=1)
    dcg = pred_data * (1. / np.log2(np.arange(2, k + 2)))
    dcg = np.sum(dcg, axis=1)
    idcg[idcg == 0.] = 1.
    ndcg = dcg / idcg
    ndcg[np.isnan(ndcg)] = 0.
    bdcg = np.sum(ndcg)
    return ret['precision'], ret['recall'], bdcg