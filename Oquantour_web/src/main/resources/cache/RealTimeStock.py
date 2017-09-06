import sys
import tushare as ts

df = ts.get_today_all()
path = sys.argv[1]
df.to_csv(path, encoding="utf8")
