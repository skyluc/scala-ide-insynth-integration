package ch.epfl.insynth.test.completion

import scala.collection.JavaConversions
import scala.collection.JavaConverters
import org.junit.Assert._
import org.junit.Test
import org.junit.BeforeClass
import org.junit.Ignore
import scala.tools.eclipse.testsetup.SDTTestUtils
import scala.tools.eclipse.javaelements.ScalaCompilationUnit
import scala.tools.nsc.interactive.Response
import scala.tools.eclipse.ScalaWordFinder
import scala.tools.nsc.util.SourceFile
import scala.tools.eclipse.ScalaPresentationCompiler
import org.eclipse.jface.text.contentassist.ICompletionProposal
import scala.tools.eclipse.testsetup.TestProjectSetup
import org.eclipse.jdt.core.search.{ SearchEngine, IJavaSearchConstants, IJavaSearchScope, SearchPattern, TypeNameRequestor }
import org.eclipse.jdt.core.IJavaElement
import scala.tools.nsc.util.OffsetPosition
import scala.tools.eclipse.completion.ScalaCompletions
import scala.tools.eclipse.completion.CompletionProposal
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.core.runtime.NullProgressMonitor
import scala.tools.eclipse.testsetup.TestProjectSetup
import ch.epfl.insynth.core.completion.InsynthCompletionProposalComputer
import ch.epfl.insynth.core.completion.InnerFinder
import ch.epfl.insynth.core.Activator
import ch.epfl.insynth.core.preferences.InSynthConstants
import org.junit.AfterClass

// tests for the project folder InSynth in the test-workspace, 5 snippets limits

object InSynthCompletionClassicCodeTests {
  
  @BeforeClass
  def setup() {
    import InSynthConstants._
    val preferenceStore = Activator.getDefault.getPreferenceStore
    		
		// we want classic code style
		preferenceStore.setValue(CodeStyleParenthesesPropertyString, CodeStyleParenthesesClassic)
		Activator.getDefault.getPreferenceStore.setValue(InSynthConstants.OfferedSnippetsPropertyString, 5)  
  }
  
  @AfterClass
  def postUpdate() {
    import InSynthConstants._
    val preferenceStore = Activator.getDefault.getPreferenceStore
    		
    val defaultStyleValue = preferenceStore getDefaultString CodeStyleParenthesesPropertyString
		// we want classic code style
		preferenceStore.setValue(CodeStyleParenthesesPropertyString, defaultStyleValue)
  }
  
}

class InSynthCompletionClassicCodeTests {
	val testProjectSetup = new CompletionUtility(InSynthCompletionTests)
	
	import testProjectSetup._

  @Test
  def testExample1() {
    val oraclePos11 = List("A.m()", "0")
    
    val exampleCompletions = List(CheckContains(oraclePos11), CheckNumberOfCompletions(5))
    
    checkCompletions("examplepkg1/Example1.scala")(exampleCompletions)
  }
  
  @Test
  def testExample2() {
    val oraclePos14 = List("new A().a", "new A().m(b)")
    
    val exampleCompletions = List(CheckContains(oraclePos14))
    
    checkCompletions("examplepkg2/Example2.scala")(exampleCompletions)
  }
  
  @Test
  def testExample3() {
    val oraclePos12regex = List("new A\\(\\)\\.m1\\(\\s*(\\S+) => new A\\(\\)\\.m2\\(\\1\\)\\s*\\)",
        "new A\\(\\)\\.m1\\(\\s*(\\S+) => new A\\(\\)\\.m2\\(l1\\)\\s*\\)")
    val oraclePos12strings = List("\"?\"")
    
    val exampleCompletions = List(CheckRegexContains(oraclePos12regex), CheckContains(oraclePos12strings))
    
    checkCompletions("examplepkg3/Example3.scala")(exampleCompletions)
  }
  
  @Test
  def interesting1 {
    val oraclePos7 = List( "(\\S+) => f\\(\\1\\)\\(\\1\\)" )
    
    val exampleCompletions = List( CheckRegexContains(oraclePos7) )
    
    checkCompletions("interesting1/Interesting1.scala")(exampleCompletions)
  }

}