# 导入必要的包
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# 加载.csv文件
data = pd.read_csv('data.csv')

# 处理分类变量，例如gender和education
data['gender'] = data['gender'].map({'male': 0, 'female': 1})
data['education'] = data['education'].map({'high_school': 0, 'bachelor': 1, 'master': 2, 'phd': 3})

# 确定特征和目标变量
features = data[["age", "gender", "education", "income"]]
target = data["purchased"]

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(features, target, test_size=0.2)

# 建立模型并训练
model = RandomForestClassifier()
model.fit(X_train, y_train)

# 获取预测结果并评估
predictions = model.predict(X_test)
accuracy = accuracy_score(y_test, predictions)
print(f'模型准确度: {accuracy}')