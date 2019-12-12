echo "Running program"

PLAYERS=$(( $RANDOM % 9 + 2 ))

for i in {1...5}
do  
	java -classpath out/production/FEUP-AIAD:jade.jar main.Main $PLAYERS $(( $RANDOM % (21 - $PLAYERS) + $PLAYERS )) $(( $RANDOM % (21 - $PLAYERS) + $PLAYERS ))
done
