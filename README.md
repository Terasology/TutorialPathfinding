# TutorialPathfinding
A tutorial on Terasology's various pathfinding topics. It has various features that enable debugging of pathfinding issues easier.

## Floor Highlighting
This module highlights different floors using different alphablend overlays. The id of the floor appears as a hovering number above the respective floor. Currently, it is implemented in just one Chunk. Entrances to the floors are highlighted using a dark overlay. Floors are highlighted with alternating overlays of red, green and ble. As you place blocks, you can see the floors merge and break apart in real time.

## Instructions

### Paths
Inventory Item 1 is a start path item, and Inventory item 2 is an end Path item. Activate Item 1 at the point where you want the path to start, go to the target location, and activate Item 2, you should be able to see a path highlighted in pink between the two points. 
### findBlock()
Inventory Item 3 is the findBlock() tester. It highlights the block that is the output of the findBlock() at the position of the player.
### Spawn Entities
Inventory Item 5 is the spawner block, and Inventory Item 6 is the target block. Place both of them at different positions. Item 4 is the spawn entity item. Activate it and you should see a floating cube spawn at the spawner and make its way to the target block.
