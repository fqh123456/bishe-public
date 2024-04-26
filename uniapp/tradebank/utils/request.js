const BASE_URL = 'http://localhost:9090'

export const request = (options => {
	return new Promise((resolve, reject) => {
		uni.request({
			url: BASE_URL + options.url,
			method: options.method || 'GET',
			header: {
				token: uni.getStorageSync('user') ? uni.getStorageSync('user').token : ''
			},
			data: options.data || {},
			success: (res) => {
				const data = res.data
				resolve(data)
			},
			fail:(error)=>{
				uni.showToast({
					icon:'error',
					title:'系统错误'
				})
				reject(error)
			}
		})
	})
})

