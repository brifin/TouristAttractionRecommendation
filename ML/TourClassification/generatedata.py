import math
import random
import numpy as np

group = 0

data_amount = 1500
# 每一步移动的距离范围
move_range = [-2.0, 2.0]
# 旅游的地点数目
tour_amount = 20
# 每个团的记录次数
record = 10

# 移动时间
def move_time(distance):
    bias = random.uniform(-30, 30)
    return abs(distance) * 67 + bias


if group == 0:
    # 停留时间
    stay_time = [10, 30]
    # 团人数
    people = [5, 20]
    # 团人数浮动
    float_num = [-1, 2]
    # 每个地点停留的位置偏差
    pos_bias = [-0.0001, 0.0001]
    # 每到达一个地点的时间偏差
    time_bias = [-5, 5]

    # # 停留时间
    # stay_time = [5, 45]
    # # 团人数
    # people = [2, 10]
    # # 每个地点停留的位置偏差
    # pos_bias = [-0.0003, 0.0003]
    # # 每到达一个地点的时间偏差
    # time_bias = [-5, 5]
    # # 团人数浮动
    # float_num = [-1, 2]

    f = open('positive_example', 'w')
    # f = open('negetive_example', 'w')

    for cur in range(data_amount):
        pos = [[0, 0]]
        time = [0]
        for i in range(tour_amount - 1):
            step_move_latitude = random.uniform(move_range[0], move_range[1])
            step_move_longitude = random.uniform(move_range[0], move_range[1])
            pos.append([pos[i][0] + step_move_latitude, pos[i][1] + step_move_longitude])
            step_spend = move_time(
                math.sqrt(step_move_longitude * step_move_longitude + step_move_latitude * step_move_latitude))
            time.append(time[i] + step_spend)

        for _ in range(record):
            people_amount = random.randint(people[0], people[1])

            record_pos = [[0, 0]]
            record_time = [0]
            record_people = [people_amount]

            for i in range(tour_amount - 1):
                # noise_bias_1 = np.random.normal(0, 100, [1])
                # noise_bias_2 = np.random.normal(0, 20, [1])
                #
                # pos_latitude_bias_rand = random.uniform(pos_bias[0], pos_bias[1])
                # pos_longitude_bias_rand = random.uniform(pos_bias[0], pos_bias[1])
                # time_bias_pos_rand = move_time(
                #     math.sqrt(
                #         pos_longitude_bias_rand * pos_longitude_bias_rand + pos_latitude_bias_rand * pos_latitude_bias_rand))
                time_bias_rand = random.uniform(time_bias[0], time_bias[1])
                stay_time_rand = random.uniform(stay_time[0], stay_time[1])
                float_num_rand = random.randint(float_num[0], float_num[1])

                # record_pos.append([pos[i + 1][0] + pos_latitude_bias_rand+float(noise_bias_1)
                #                       , pos[i + 1][1] + pos_longitude_bias_rand+float(noise_bias_1)])
                # record_time.append(time[i + 1] + time_bias_rand + time_bias_pos_rand + stay_time_rand + float(noise_bias_1))
                #
                # float_num_value = record_people[i] + float_num_rand + float(noise_bias_2)
                record_pos.append([pos[i + 1][0], pos[i + 1][1]])
                record_time.append(time[i + 1] + time_bias_rand + stay_time_rand)

                float_num_value = record_people[i] + float_num_rand
                if float_num_value < people[0] or float_num_value > people[1]:
                    float_num_value = record_people[i]
                record_people.append(float_num_value)

            for i in range(tour_amount):
                f.write(str(record_pos[i][0])+','+str(record_pos[i][1])+','+str(round(record_time[i], 5)) + ',' + str(record_people[i]) + '\t')

            f.write('\n')

        f.flush()
        print(round((cur+1)/data_amount, 2), end='\r')

elif group == 1:
    # 停留时间
    stay_time = [30, 120]
    # 团人数
    people = [2, 10]
    # 每个地点停留的位置偏差
    pos_bias = [-0.01, 0.01]
    # 每到达一个地点的时间偏差
    time_bias = [-60, 60]
    # 团人数浮动
    float_num = [-1, 2]

    f = open('negetive_example', 'w')

    for cur in range(data_amount):
        pos = [[0, 0]]
        time = [0]
        for i in range(tour_amount - 1):
            step_move_latitude = random.uniform(move_range[0], move_range[1])
            step_move_longitude = random.uniform(move_range[0], move_range[1])
            pos.append([pos[i][0] + step_move_latitude, pos[i][1] + step_move_longitude])
            step_spend = move_time(
                math.sqrt(step_move_longitude * step_move_longitude + step_move_latitude * step_move_latitude))
            time.append(time[i] + step_spend)

        for _ in range(record):
            people_amount = random.randint(people[0], people[1])
            record_pos = [[0, 0]]
            record_time = [0]
            record_people = [people_amount] * tour_amount

            if cur > 9 / 10 * data_amount:
                float_people_rand = random.randint(float_num[0], float_num[1])
                change_steps = random.randint(1, 19)
                for index in range(change_steps):
                    record_people[tour_amount - 1 - index] = record_people[tour_amount - 1 - index] + float_people_rand

            for i in range(tour_amount - 1):
                pos_latitude_bias_rand = random.uniform(pos_bias[0], pos_bias[1])
                pos_longitude_bias_rand = random.uniform(pos_bias[0], pos_bias[1])
                time_bias_pos_rand = move_time(
                    math.sqrt(
                        pos_longitude_bias_rand * pos_longitude_bias_rand + pos_latitude_bias_rand * pos_latitude_bias_rand))
                time_bias_rand = random.uniform(time_bias[0], time_bias[1])
                stay_time_rand = random.uniform(stay_time[0], stay_time[1])

                record_pos.append([pos[i + 1][0] + pos_latitude_bias_rand, pos[i + 1][0] + pos_longitude_bias_rand])
                record_time.append(time[i + 1] + time_bias_rand + time_bias_pos_rand + stay_time_rand)

            for i in range(tour_amount):
                f.write(str(round(record_time[i], 5)) + ',' + str(record_people[i]) + '\t')

            f.write('\n')

        f.flush()
        print(round((cur+1)/data_amount, 2), end='\r')
