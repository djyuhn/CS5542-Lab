package openie

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
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

        // Return sentences of
        return document.get(CoreAnnotations.SentencesAnnotation::class.java)

    }
}