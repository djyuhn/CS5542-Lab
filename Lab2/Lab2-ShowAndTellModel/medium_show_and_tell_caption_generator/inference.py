from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import logging
import math
import os

import tensorflow as tf

from medium_show_and_tell_caption_generator.caption_generator import CaptionGenerator
from medium_show_and_tell_caption_generator.model import ShowAndTellModel
from medium_show_and_tell_caption_generator.vocabulary import Vocabulary

FLAGS = tf.flags.FLAGS

tf.flags.DEFINE_string("model_path", r"C:\dev\Repositories\CS5542-ICP\ICP6\medium-show-and-tell-caption-generator-master\model\show-and-tell.pb", "Model graph def path")
tf.flags.DEFINE_string("vocab_file", r"C:\dev\Repositories\CS5542-ICP\ICP6\medium-show-and-tell-caption-generator-master\etc\word_counts.txt", "Text file containing the vocabulary.")
tf.flags.DEFINE_string("input_files", r"C:\dev\BigData-Images\combined",
                       "File pattern or comma-separated list of file patterns of image files.")

logging.basicConfig(level=logging.INFO)

logger = logging.getLogger(__name__)


def main(_):
    model = ShowAndTellModel(FLAGS.model_path)
    vocab = Vocabulary(FLAGS.vocab_file)
    filenames = _load_filenames()

    output_file = open("../data/generated_captions.txt", "w")

    generator = CaptionGenerator(model, vocab, beam_size=10, max_caption_length=10)

    for filename in filenames:
        with tf.gfile.GFile(filename, "rb") as f:
            image = f.read()
        captions = generator.beam_search(image)
        print("Captions for image %s:" % os.path.basename(filename))
        output_file.write(os.path.basename(filename) + "\t")
        for i, caption in enumerate(captions):
            # Ignore begin and end tokens <S> and </S>.
            sentence = [vocab.id_to_token(w) for w in caption.sentence[1:-1]]
            sentence = " ".join(sentence)
            print("  %d) %s (p=%f)" % (i, sentence, math.exp(caption.logprob)))
            output_file.write("\t%s" % (sentence))
        output_file.write("\n")

    output_file.close()

def _load_filenames():
    filenames = []
    files = os.listdir(FLAGS.input_files)
    for file in files:
        file_path = os.path.join(FLAGS.input_files, file)
        filenames.extend(tf.gfile.Glob(file_path))
    logger.info("Running caption generation on %d files matching %s",
                len(filenames), FLAGS.input_files)
    return filenames


if __name__ == "__main__":
    tf.app.run()
