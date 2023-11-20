package Constants;

public enum Level {
    NO_FIRE(0,30), ADJ_FIRE(30, 100), ON_FIRE(100, 300),
     DEEP_FIRE(300, 600);

    public final int min;
    public final int max;

    private Level(int min, int max){
        this.min = min;
        this.max = max;
    } 

    public static Level nextLevel(Level level){
        switch (level) {
            case NO_FIRE:
                return ADJ_FIRE;
            case ADJ_FIRE:
                return ON_FIRE;
            case ON_FIRE:
                return DEEP_FIRE;
            default:
                return DEEP_FIRE;
        }
    }
}
