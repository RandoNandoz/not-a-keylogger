package model;

abstract class InputTime implements Comparable<InputTime> {
    protected long nsSinceStart;

    public long getNsSinceStart() {
        return this.nsSinceStart;
    }

    @Override
    public int compareTo(InputTime o) {
        return Long.compare(nsSinceStart, o.getNsSinceStart());
    }
}
