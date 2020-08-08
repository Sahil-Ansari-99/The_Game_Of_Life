public class Simulator {
    private int[][] states;
    private GameWorld gameWorld;
    private int n;
    private int[] drs, dcs;
    private boolean keepSimulating;

    public Simulator(int n, GameWorld gameWorld) {
        this.n = n;
        this.gameWorld = gameWorld;
        this.states = new int[n][n];
        this.drs = new int[]{1, 0, -1, 0, 1, -1, 1, -1};
        this.dcs = new int[]{0, 1, 0, -1, -1, 1, 1, -1};
        this.keepSimulating = false;
    }

    public void setAlive(int x, int y) {
        states[x][y] = 1;
    }

    public void setDead(int x, int y) {
        states[x][y] = 0;
    }

    public int[][] getStates() {
        return this.states;
    }

    private boolean isValidAndAlive(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n && states[x][y] == 1;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    private int getALiveNeighbourCount(int x, int y) {
        int count = 0;
        for (int i = 0; i < drs.length; i++) {
            if (isValidAndAlive(x + drs[i], y + dcs[i])) count++;
        }
        return count;
    }

    private void updateStates(int[][] nextStates) {
        for (int i = 0; i < n; i++) {
            System.arraycopy(nextStates[i], 0, states[i], 0, n);
        }
    }

    public void simulate() {
        keepSimulating = true;
        while (keepSimulating) {
            int[][] nextStates = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    nextStates[i][j] = 0;
                    int liveNeighbours = getALiveNeighbourCount(i, j);
                    if (states[i][j] == 1) {
                        if (liveNeighbours < 2) nextStates[i][j] = 0;
                        else if  (liveNeighbours == 2 || liveNeighbours == 3) nextStates[i][j] = 1;
                        else nextStates[i][j] = 0;
                    } else {
                        if (liveNeighbours == 3) nextStates[i][j] = 1;
                    }
                }
            }
            updateStates(nextStates);
        }
    }

    public int[][] simulateOnce() {
        int[][] nextStates = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nextStates[i][j] = 0;
                int liveNeighbours = getALiveNeighbourCount(i, j);
                if (states[i][j] == 1) {
                    if (liveNeighbours < 2) nextStates[i][j] = 0;
                    else if  (liveNeighbours == 2 || liveNeighbours == 3) nextStates[i][j] = 1;
                    else nextStates[i][j] = 0;
                } else {
                    if (liveNeighbours == 3) nextStates[i][j] = 1;
                }
            }
        }
        updateStates(nextStates);
        return states;
    }

    public void resetStates() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                states[i][j] = 0;
            }
        }
    }
}
