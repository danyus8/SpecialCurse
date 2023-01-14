package org.example.gameFifteen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class FifteenPuzzle {

    private static class TilePos {
        public int x;
        public int y;

        public TilePos(int x, int y) {
            this.x=x;
            this.y=y;
        }

    }
    public final static int DIMS=3;
    private final int[][] tiles;
    private final int display_width;
    private TilePos blank;

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
        blank = new TilePos(DIMS - 1, DIMS - 1);
        tiles[blank.x][blank.y]=0;
    }

    public final static FifteenPuzzle SOLVED=new FifteenPuzzle();


    public FifteenPuzzle(FifteenPuzzle toClone) {
        this();  // chain to basic init
        for(TilePos p: allTilePos()) {
            tiles[p.x][p.y] = toClone.tile(p);
        }
        blank = toClone.getBlank();
    }

    public List<TilePos> allTilePos() {
        ArrayList<TilePos> out = new ArrayList<>();
        for(int i=0; i<DIMS; i++) {
            for(int j=0; j<DIMS; j++) {
                out.add(new TilePos(i, j));
            }
        }
        return out;
    }


    public int tile(TilePos p) {
        return tiles[p.x][p.y];
    }


    public TilePos getBlank() {
        return blank;
    }

    public TilePos whereIs(int x) {
        for(TilePos p: allTilePos()) {
            if( tile(p) == x ) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof FifteenPuzzle) {
            for(TilePos p: allTilePos()) {
                if( this.tile(p) != ((FifteenPuzzle) o).tile(p)) {
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
        for(TilePos p: allTilePos()) {
            out= (out*DIMS*DIMS) + this.tile(p);
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


    public List<TilePos> allValidMoves() {
        ArrayList<TilePos> out = new ArrayList<>();
        for(int dx=-1; dx<2; dx++) {
            for(int dy=-1; dy<2; dy++) {
                TilePos tp = new TilePos(blank.x + dx, blank.y + dy);
                if( isValidMove(tp) ) {
                    out.add(tp);
                }
            }
        }
        return out;
    }

    public boolean isValidMove(TilePos p) {
        if( ( p.x < 0) || (p.x >= DIMS) ) {
            return false;
        }
        if( ( p.y < 0) || (p.y >= DIMS) ) {
            return false;
        }
        int dx = blank.x - p.x;
        int dy = blank.y - p.y;
        return (Math.abs(dx) + Math.abs(dy) == 1) && (dx * dy == 0);
    }


    public void move(TilePos p) {
        if( !isValidMove(p) ) {
            throw new RuntimeException("Invalid move");
        }
        assert tiles[blank.x][blank.y]==0;
        tiles[blank.x][blank.y] = tiles[p.x][p.y];
        tiles[p.x][p.y]=0;
        blank = p;
    }

    public FifteenPuzzle moveClone(TilePos p) {
        FifteenPuzzle out = new FifteenPuzzle(this);
        out.move(p);
        return out;
    }

    public void shuffle(int howmany) {
        for(int i=0; i<howmany; i++) {
            List<TilePos> possible = allValidMoves();
            int which =  (int) (Math.random() * possible.size());
            TilePos move = possible.get(which);
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
        for(TilePos p: allTilePos()) {
            int val = tile(p);
            if( val > 0 ) {
                TilePos correct = SOLVED.whereIs(val);
                sum += Math.abs( correct.x = p.x );
                sum += Math.abs( correct.y = p.y );
            }
        }
        return sum;
    }


    public List<FifteenPuzzle> allAdjacentPuzzles() {
        ArrayList<FifteenPuzzle> out = new ArrayList<>();
        for( TilePos move: allValidMoves() ) {
            out.add( moveClone(move) );
        }
        return out;
    }

    public List<FifteenPuzzle> aStarSolve() {
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
        System.out.println("FifteenGamePuzzle started:  ");

        FifteenPuzzle p = new FifteenPuzzle();
        p.shuffle(40);  // При большом значении > 40 не получилось получить стабильный результат из-за ограниченности в вычислительной мощности
        System.out.println("Shuffled board:  ");
        p.show();

        List<FifteenPuzzle> solution;

        System.out.println("Solving with A*  ");
        solution = p.aStarSolve();
        showSolution(solution);

    }
}