package block;

public class Range {
	int start;
	int end;

	public Range(int s, int e) {
		if (s <= e) {
			start = s;
			end = e;
		} else {
			start = e;
			end = s;
		}

	}

	public Range() {
		start = 0;
		end = 0;
	}

	public int start() {
		return start;
	}
	
	public void setStart(int s){
		start=s;
	}

	public int end() {
		return end;
	}

	public boolean isContainedIn(Range other) {
		return (other.start() <= start()
				&& other.end() >= end());
	}
	
	public boolean isEmpty(){
		return start() == end();
	}

	public boolean overlaps(Range other) {
		return (other.start() < end() && other.start() > start())
				|| (other.end() > start() && end() > other.end())
				|| other.isContainedIn(this);
	}
	
	public String toString(){
		return "["+start()+","+end()+"]";
	}
}
