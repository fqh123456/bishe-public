import pandas as pd
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt

# 读取csv文件，将其读入一个 pandas DataFrame
url = "E:/CSV/train_set.csv"
df = pd.read_csv(url)

# 使用年龄和余额对客户进行分群
X = df[['age', 'balance']]

# 使用KMeans找和合适的客户群数
wcss = []
for i in range(1, 11):
    kmeans = KMeans(n_clusters=i, init='k-means++', max_iter=300, n_init=10, random_state=0)
    kmeans.fit(X)
    wcss.append(kmeans.inertia_)
    print(wcss)
plt.plot(range(1, 11), wcss)
plt.title('Elbow Method')
plt.xlabel('Number of clusters')
plt.ylabel('WCSS')
plt.show()

#根据Elboe Method的结果选择合适的簇数，然后用K-means算法进行分群
n_clusters = 5 # 假设我们选择了5
kmeans = KMeans(n_clusters=n_clusters, init='k-means++', max_iter=300, n_init=10, random_state=0)
y_kmeans = kmeans.fit_predict(X)
print(y_kmeans)

# 在散点图上显示出所有客户分群结果
plt.scatter(X[y_kmeans == 0]['age'], X[y_kmeans == 0]['balance'], s=100, c='red', label='Cluster 1')
plt.scatter(X[y_kmeans == 1]['age'], X[y_kmeans == 1]['balance'], s=100, c='blue', label='Cluster 2')
plt.scatter(X[y_kmeans == 2]['age'], X[y_kmeans == 2]['balance'], s=100, c='green', label='Cluster 3')
plt.scatter(X[y_kmeans == 3]['age'], X[y_kmeans == 3]['balance'], s=100, c='cyan', label='Cluster 4')
plt.scatter(X[y_kmeans == 4]['age'], X[y_kmeans == 4]['balance'], s=100, c='magenta', label='Cluster 5')
plt.scatter(kmeans.cluster_centers_[:,0], kmeans.cluster_centers_[:,1], s=300, c='yellow', label='Centroids')
plt.title('Clusters of customers')
plt.xlabel('Age')
plt.ylabel('Balance')
plt.legend()
plt.show()