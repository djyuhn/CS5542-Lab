import numpy as np #supporting multi-dimensional arrays and matrices
import os #read or write a file
import cv2
import pandas as pd #data manipulation and analysis
from tqdm import tqdm # for  well-established ProgressBar
from random import shuffle #only shuffles the array along the first axis of a multi-dimensional array. The order of sub-arrays is changed but their contents remains the same.
import tensorflow as tf


''' function that accept category and return array format of the vlaue , one-hot array
 am sure there's better way to do this .......'''

def label_img(word_label):
    if word_label == 'architecture': return [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'bridge': return [0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'building': return [0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'canal': return [0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'canyon': return [0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'cave': return [0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'city': return [0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'coast': return [0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'desert': return [0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'farm': return [0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'forest': return [0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'hill': return [0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'house': return [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'lake': return [0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'library': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'mall': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0]
    elif word_label == 'marketplace': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0]
    elif word_label == 'mountain': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0]
    elif word_label == 'ocean': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0]
    elif word_label == 'plain': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0]
    elif word_label == 'river': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0]
    elif word_label == 'skyscraper': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0]
    elif word_label == 'town': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0]
    elif word_label == 'village': return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0]

'''function that will create train data , will go thought all the file do this 
----read the image in  grayscale mode ,resize it
---change it to numpy arrays and  append it to dataframe train with it`s associated category '''

def create_train_data(CATEGORIES,train_dir,IMG_SIZE):
    train = []
    for category_id, category in enumerate(CATEGORIES):
        for img in tqdm(os.listdir(os.path.join(train_dir, category))):
            label=label_img(category)
            path=os.path.join(train_dir,category,img)
            img=cv2.imread(path,cv2.IMREAD_GRAYSCALE)
            img = cv2.resize(img, (IMG_SIZE,IMG_SIZE))
            train.append([np.array(img),np.array(label)])
    shuffle(train)
    return train

'''function that will create train data , will go thought all the file do this 
----read the image in  grayscale mode ,resize it
---change it to numpy arrays and  append it to dataframe train with it`s associated category
---images are labeled '''


def create_test_labeled_data(CATEGORIES,test_dir,IMG_SIZE):
    test = []
    for category_id, category in enumerate(CATEGORIES):
        for img in tqdm(os.listdir(os.path.join(test_dir, category))):
            label=category+","+img
            path=os.path.join(test_dir,category,img)
            img=cv2.imread(path,cv2.IMREAD_GRAYSCALE)
            img = cv2.resize(img, (IMG_SIZE,IMG_SIZE))
            test.append([np.array(img),label])
    shuffle(test)
    return test



'''function that will create validation data , will go thought  file do this 
----read the image in  grayscale mode ,resize it
---change it to numpy arrays and  append it to dataframe validation but no category here of course  
---images are unlabeled '''

def create_test_unlabeled_data(test_dir,IMG_SIZE):
    test = []
    for img in tqdm(os.listdir(test_dir)):
        path = os.path.join(test_dir, img)
        img_num = img
        img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
        if img is not None:
            img = cv2.resize(img, (IMG_SIZE, IMG_SIZE))
            test.append([np.array(img), img_num])
        else:
            print("image not loaded")

    shuffle(test)
    return test


# return Indexes of the maximal elements of a array
def label_return(model_out):
    if np.argmax(model_out) == 0:
        return 'architecture'
    elif np.argmax(model_out) == 1:
        return 'bridge'
    elif np.argmax(model_out) == 2:
        return 'building'
    elif np.argmax(model_out) == 3:
        return 'canal'
    elif np.argmax(model_out) == 4:
        return 'canyon'
    elif np.argmax(model_out) == 5:
        return 'cave'
    elif np.argmax(model_out) == 6:
        return 'city'
    elif np.argmax(model_out) == 7:
        return 'coast'
    elif np.argmax(model_out) == 8:
        return 'desert'
    elif np.argmax(model_out) == 9:
        return 'farm'
    elif np.argmax(model_out) == 10:
        return 'forest'
    elif np.argmax(model_out) == 11:
        return 'hill'
    elif np.argmax(model_out) == 12:
        return 'house'
    elif np.argmax(model_out) == 13:
        return 'lake'
    elif np.argmax(model_out) == 14:
        return 'library'
    elif np.argmax(model_out) == 15:
        return 'mall'
    elif np.argmax(model_out) == 16:
        return 'marketplace'
    elif np.argmax(model_out) == 17:
        return 'mountain'
    elif np.argmax(model_out) == 18:
        return 'ocean'
    elif np.argmax(model_out) == 19:
        return 'plain'
    elif np.argmax(model_out) == 20:
        return 'river'
    elif np.argmax(model_out) == 21:
        return 'skyscraper'
    elif np.argmax(model_out) == 22:
        return 'town'
    elif np.argmax(model_out) == 23:
        return 'village'

