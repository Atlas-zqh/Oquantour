import sys
import tushare as ts

df = ts.get_index()
path = sys.argv[1]
df.to_csv(path, encoding="utf8")
