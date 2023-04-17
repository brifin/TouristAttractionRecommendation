from model import *
from utils import *


with open('arg.pickle', 'rb') as file:
    arg = pickle.load(file)

classification = hmt_grn(arg)
classification.load_weights('trained_model')

arg['novelEval'] = False
evaluate(classification, arg)

arg['novelEval'] = True
evaluate(classification, arg)