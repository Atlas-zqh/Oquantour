import sys
import tushare as ts

df = ts.get_h_data(sys.argv[2], start=sys.argv[4], end=sys.argv[4], autype=sys.argv[3])
path = sys.argv[1]
df.to_csv(path, encoding="utf8")
