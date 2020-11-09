public class ShardCoordImpl implements ShardCoordInterface {
    public static final ShardCoordImpl instance = new ShardCoordImpl();
    public int numShards;
    public String getShardId(String key) {
        int num = Integer.parseInt(key.substring(2));
        return Integer.toString(num%this.numShards);
    }
}