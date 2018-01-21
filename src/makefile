JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	./graphCode/Edge.java \
	./graphCode/GraphInput.java \
	./graphCode/InputLib.java \
	./graphCode/KeyboardReader.java \
	./graphCode/SimpleGraph.java \
	./graphCode/Vertex.java \
	./graphCode/FordFulkerson.java \
	./graphCode/ScalingFordFulkerson.java \
	./graphCode/PreflowPush.java \
	tcss543.java \
	./graphGenerationCode/Bipartite/BipartiteGraph.java \
	./graphGenerationCode/FixedDegree/RandomGraph.java \
	./graphGenerationCode/Mesh/MeshGenerator.java \
	./graphGenerationCode/Random/BuildGraph.java \


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
