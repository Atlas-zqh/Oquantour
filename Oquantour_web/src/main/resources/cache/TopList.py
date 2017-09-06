import sys
import tushare as ts

df = ts.top_list(sys.argv[2])
path = sys.argv[1]
df.to_csv(path, encoding="utf8")
