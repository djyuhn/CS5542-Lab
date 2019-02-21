package openie

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.simple.Document
import edu.stanford.nlp.util.CoreMap
import java.util.*

/**
 * @author djyuhn
 * 2/19/2019
 */
object CoreNLP {
    fun returnNLPText(text: String) : List<CoreMap> {

        // Create StanfordCoreNLP object
        val props = Properties()
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref,depparse,natlog,openie")
        val pipeline = StanfordCoreNLP(props)

        // Create annotation with given text
        val document = Annotation(text)

        // Run annotators on text
        pipeline.annotate(document)

        // Return sentences
        return document.get(CoreAnnotations.SentencesAnnotation::class.java)

    }

    fun returnLemma(text: String) : String {

        val document = Document(text)
        var lemma = ""
        for (sentence in document.sentences()) {
            val lemmaList: List<String> = sentence.lemmas()
            for (i in 0 until lemmaList.size) {
                lemma += lemmaList[i] + " "
            }
        }
        return lemma
    }
}