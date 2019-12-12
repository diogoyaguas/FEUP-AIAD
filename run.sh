echo "Running program"

for i in {1..1000}
do  
	PLAYERS=$(( $RANDOM % 9 + 2 ))
	java -classpath out/production/FEUP-AIAD:jade.jar main.Main $PLAYERS $(( $RANDOM % (21 - $PLAYERS) + $PLAYERS )) $(( $RANDOM % (21 - $PLAYERS) + $PLAYERS ))
done
