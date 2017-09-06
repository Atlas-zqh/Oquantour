import sys
import tushare as ts

year = int(sys.argv[1])
quarter = int(sys.argv[2])

df = ts.get_report_data(year, quarter)
path = sys.argv[3]
df.to_csv(path, encoding="utf8")

df = ts.get_profit_data(year, quarter)
path = sys.argv[4]
df.to_csv(path, encoding="utf8")

df = ts.get_operation_data(year, quarter)
path = sys.argv[5]
df.to_csv(path, encoding="utf8")

df = ts.get_growth_data(year, quarter)
path = sys.argv[6]
df.to_csv(path, encoding="utf8")

df = ts.get_debtpaying_data(year, quarter)
path = sys.argv[7]
df.to_csv(path, encoding="utf8")

df = ts.get_cashflow_data(year, quarter)
path = sys.argv[8]
df.to_csv(path, encoding="utf8")
