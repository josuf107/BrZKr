brzkr.jar : src/main/java/io/github/josuf107/brzkr/BrZKr.java plugin.yml
	javac -cp ~/.m2/repository/org/bukkit/bukkit/1.5.2-R0.2-SNAPSHOT/bukkit-1.5.2-R0.2-SNAPSHOT.jar src/main/java/io/github/josuf107/brzkr/BrZKr.java
	jar -cvf brzkr.jar plugin.yml -C src/main/java/ io 
