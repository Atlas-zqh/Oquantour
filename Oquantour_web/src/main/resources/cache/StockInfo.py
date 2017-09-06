import sys
import tushare as ts

df = ts.get_h_data(sys.argv[2], start='2005-01-01', end='2018-01-01', autype=sys.argv[3])
path = sys.argv[1]
df.to_csv(path, encoding="utf8")
