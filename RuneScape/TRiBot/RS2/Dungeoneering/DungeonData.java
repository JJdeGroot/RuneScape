package scripts;

import java.awt.Point;

public class DungeonData {
    public final Point[] room = new Point[16];
    public final int[] location = new int[4];
    public boolean[][] open = new boolean[16][4];

    public DungeonData(Point room, int location, boolean open) {
        this.room[room.x/4 + (room.y - (room.x/4))] = room;
        this.location[location] = location;
        this.open[room.x/4 + (room.y - (room.x/4))][location] = open;
    }
}