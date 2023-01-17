package org.example.gameFifteen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class FifteenPuzzle {

    private static class TilePosition {
        public int x;
        public int y;

        public TilePosition(int x, int y) {
            this.x=x;
            this.y=y;
        }

    }
    public final static int DIMS=4;
    private final int[][] tiles;
    private final int display_width;
    private TilePosition blank;

    public FifteenPuzzle() {
        tiles = new int[DIMS][DIMS];
        int cnt=1;
        for(int i=0; i<DIMS; i++) {
            for(int j=0; j<DIMS; j++) {
                tiles[i][j]=cnt;
                cnt++;
            }
        }
        display_width=Integer.toString(cnt).length();

        // init blank
        blank = new TilePosition(DIMS - 1, DIMS - 1);
        tiles[blank.x][blank.y]=0;
    }

    public final static FifteenPuzzle SOLVED=new FifteenPuzzle();


    public FifteenPuzzle(FifteenPuzzle toClone) {
        this();  // chain to basic init
        for(TilePosition position : allTilePositions()) {
            tiles[position.x][position.y] = toClone.tile(position);
        }
        blank = toClone.getBlank();
    }

    public List<TilePosition> allTilePositions() {
        ArrayList<TilePosition> out = new ArrayList<>();
        for(int i=0; i<DIMS; i++) {
            for(int j=0; j<DIMS; j++) {
                out.add(new TilePosition(i, j));
            }
        }
        return out;
    }


    public int tile(TilePosition position) {
        return tiles[position.x][position.y];
    }


    public TilePosition getBlank() {
        return blank;
    }

    public TilePosition whereIs(int x) {
        for(TilePosition position : allTilePositions()) {
            if( tile(position) == x ) {
                return position;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof FifteenPuzzle) {
            for(TilePosition position : allTilePositions()) {
                if( this.tile(position) != ((FifteenPuzzle) o).tile(position)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int out=0;
        for(TilePosition position : allTilePositions()) {
            out= (out*DIMS*DIMS) + this.tile(position);
        }
        return out;
    }

    public void show() {
        System.out.println("============  ");
        for(int i=0; i<DIMS; i++) {
            System.out.print("| ");
            for(int j=0; j<DIMS; j++) {
                int n = tiles[i][j];
                StringBuilder s;
                if( n>0) {
                    s = new StringBuilder(Integer.toString(n));
                } else {
                    s = new StringBuilder();
                }
                while( s.length() < display_width ) {
                    s.append(" ");
                }
                System.out.print(s + "| ");
            }
            System.out.print("\n");
        }
        System.out.print("============  \n\n");
    }


    public List<TilePosition> allValidMoves() {
        ArrayList<TilePosition> out = new ArrayList<>();
        for(int dx=-1; dx<2; dx++) {
            for(int dy=-1; dy<2; dy++) {
                TilePosition tilePosition = new TilePosition(blank.x + dx, blank.y + dy);
                if( isValidMove(tilePosition) ) {
                    out.add(tilePosition);
                }
            }
        }
        return out;
    }

    public boolean isValidMove(TilePosition position) {
        if( ( position.x < 0) || (position.x >= DIMS) ) {
            return false;
        }
        if( ( position.y < 0) || (position.y >= DIMS) ) {
            return false;
        }
        int dx = blank.x - position.x;
        int dy = blank.y - position.y;
        return (Math.abs(dx) + Math.abs(dy) == 1) && (dx * dy == 0);
    }


    public void move(TilePosition position) {
        if( !isValidMove(position) ) {
            throw new RuntimeException("Invalid move");
        }
        assert tiles[blank.x][blank.y]==0;
        tiles[blank.x][blank.y] = tiles[position.x][position.y];
        tiles[position.x][position.y]=0;
        blank = position;
    }

    public FifteenPuzzle moveClone(TilePosition position) {
        FifteenPuzzle out = new FifteenPuzzle(this);
        out.move(position);
        return out;
    }

    public void shuffle(int shuffleAmount) {
        for(int i = 0; i< shuffleAmount; i++) {
            List<TilePosition> possible = allValidMoves();
            int which =  (int) (Math.random() * possible.size());
            TilePosition move = possible.get(which);
            this.move(move);
        }
    }


    public int numberMisplacedTiles() {
        int wrong=0;
        for(int i=0; i<DIMS; i++) {
            for(int j=0; j<DIMS; j++) {
                if( (tiles[i][j] >0) && ( tiles[i][j] != SOLVED.tiles[i][j] ) ){
                    wrong++;
                }
            }
        }
        return wrong;
    }


    public boolean isSolved() {
        return numberMisplacedTiles() == 0;
    }

    public int manhattanDistance() {
        int sum=0;
        for(TilePosition position : allTilePositions()) {
            int val = tile(position);
            if( val > 0 ) {
                TilePosition correct = SOLVED.whereIs(val);
                sum += Math.abs( correct.x = position.x );
                sum += Math.abs( correct.y = position.y );
            }
        }
        return sum;
    }


    public List<FifteenPuzzle> allAdjacentPuzzles() {
        ArrayList<FifteenPuzzle> out = new ArrayList<>();
        for( TilePosition move: allValidMoves() ) {
            out.add( moveClone(move) );
        }
        return out;
    }

    public List<FifteenPuzzle> Solver() {
        HashMap<FifteenPuzzle,FifteenPuzzle> predecessor = new HashMap<>();
        HashMap<FifteenPuzzle,Integer> depth = new HashMap<>();
        final HashMap<FifteenPuzzle,Integer> score = new HashMap<>();
        Comparator<FifteenPuzzle> comparator = Comparator.comparingInt(score::get);
        PriorityQueue<FifteenPuzzle> toVisit = new PriorityQueue<>(10000, comparator);

        predecessor.put(this, null);
        depth.put(this,0);
        score.put(this, this.manhattanDistance());
        toVisit.add(this);
        int cnt=0;
        while( toVisit.size() > 0) {
            FifteenPuzzle candidate = toVisit.remove();
            cnt++;
            if( cnt % 10000 == 0) {
                System.out.printf("Considered %,d positions. Queue = %,d  \n", cnt, toVisit.size());
            }
            if( candidate.isSolved() ) {
                System.out.printf("Solution considered %d boards  \n", cnt);
                LinkedList<FifteenPuzzle> solution = new LinkedList<>();
                FifteenPuzzle backtrace=candidate;
                while( backtrace != null ) {
                    solution.addFirst(backtrace);
                    backtrace = predecessor.get(backtrace);
                }
                return solution;
            }
            for(FifteenPuzzle fp: candidate.allAdjacentPuzzles()) {
                if( !predecessor.containsKey(fp) ) {
                    predecessor.put(fp,candidate);
                    depth.put(fp, depth.get(candidate)+1);
                    int estimate = fp.manhattanDistance();
                    score.put(fp, depth.get(candidate)+1 + estimate);
                    // don't' add to p-queue until the metadata is in place that the comparator needs
                    toVisit.add(fp);
                }
            }
        }
        return null;
    }

    private static void showSolution(List<FifteenPuzzle> solution) {
        if (solution != null ) {
            System.out.printf("Success!  Solution with %d moves:  \n", solution.size());
            for( FifteenPuzzle sp: solution) {
                sp.show();
            }
        } else {
            System.out.println("Did not solve. Try to run with more powerful executor  ");
        }
    }
    public static void main(String[] args) {
        // set
        FifteenPuzzle p = new FifteenPuzzle();

        p.shuffle(40);  // При большом значении > 40 не получилось получить стабильный результат из-за ограниченности в вычислительной мощности
        p.show();

        // solve
        List<FifteenPuzzle> solution;
        solution = p.Solver();

        //result
        showSolution(solution);

    }
}