<template >
	<view  >
		<u-sticky style="background-color: #fff;">
			<u-subsection class="subsection" :list="list" mode="button" :current="current" @change="sectionChange"></u-subsection>
		    <view style="display: flex;margin: 0 auto;">
				<view v-for="(item,index) in list1" style="width: 100%;display: flex;">
					<text style="padding-left: 10%;padding-top: 5%;">{{item.name}}</text>
					<view style="padding-top:6%;">
						<u-icon color="rgba(7,212,254)" v-if="index==selectone.index&&selectone.direct==1" size="10" name="arrow-up-fill"></u-icon>
						<u-icon v-if="!(index==selectone.index&&selectone.direct==1)" size="10" name="arrow-up-fill" @click="setchose2(index,1)"></u-icon>
						<u-icon v-if="!(index==selectone.index&&selectone.direct==2)" size="10" name="arrow-down-fill" @click="setchose2(index,2)"></u-icon>
						<u-icon color="rgba(7,212,254)" v-if="index==selectone.index&&selectone.direct==2" size="10" name="arrow-down-fill"></u-icon>
					</view>
				</view>
				
			</view>
			<u-divider lineColor="rbga(40,44,53)" ></u-divider>
		</u-sticky>
		<view v-for="(item,index) in productchose">
			<view class="box">
				<view style="margin-left: 3%;margin-top: 3%;">
					<view @click="intproduct(item)">
						<view style="display: flex;" >
							<u--text prefixIcon="baidu" size="14" bold :text="item.name">	</u--text>
							<u--text v-if="item.type==1"  size="12" text="固期产品" ></u--text>
							<u--text v-if="item.type==2"  size="12" text="限期产品" ></u--text>
						</view>
						<view style="display: flex;margin-top: 2%;">
							<u--text style="flex: 2;" size="20" color="red" bold :text="item.rate+'%'">	</u--text>
						    <u--text v-if="item.type==1" style="flex:3" size="15" bold :text="'封闭'+item.minday+'天'" ></u--text>
							<u--text v-if="item.type==2" style="flex:3" size="15" bold :text="'最低持有'+item.minday+'天'" ></u--text>
						</view>
						<view style="display: inline-flex; margin-top: 1%;width: 100%;">
							<u--text style="flex: 4;"  type="info" size="12" text="成立以来年化">	</u--text>
						    <u--text style="flex: 4;" size="12" type="info" :text="item.price+'元起购'" ></u--text>
							<u--text style="flex: 2;" size="12" :type="risk[index].color" :text="'R'+item.risk+risk[index].text"></u--text>
						</view>
						<view style="display: flex;margin-top: 1%;">
							<u-line-progress style="flex: 4;margin-right: 5%;" :percentage="(item.sum/item.amount)*100" ></u-line-progress>
							<u--text size="11" style="flex: 1;" type="info" text="总购买进度"></u--text>
						</view>
					</view>
					<view style="margin: 0 auto;margin-top: 1%;width: 90%;">
						 <u-collapse>
							 <u-collapse-item
							       title="详情描述"
							     >
							       <text class="u-collapse-content">{{item.description}}</text>
							     </u-collapse-item>
						  </u-collapse>
					</view>
					<u--text style="padding-bottom: 2%;" color="rgba(245,136,0)" size="13" text="业绩比较基准不是预期收益率,不代表产品得未来表现和实际收益,不构成对产品收益的承诺"></u--text>
				</view>
				
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		
		data() {
			return {
				selectone:{index:0,direct:2},
				products:uni.getStorageSync('products'),
				productchose:[],
				risk:[],
				list: ['全部','固定期限', '限定期限'],
				current: 0,
				list1:[{name:'收益率',select:1},{name:'起购金额',select:1},{name:'期限',select:1},{name:'风险等级',select:1}]
			}
		},
		onLoad() {
			this.getproducts()
		},
		onPullDownRefresh(){
			this.getproducts()
		},
		methods: {
			getproducts(){//获取所有产品
				this.request({
					url:"/Product/getProduct",
					method:"POST",
					data:{
					}
				}).then(res=>{
					if(res.code==='200'){
						uni.setStorageSync('products',res.data)
						this.products=res.data
						this.productchose=res.data
						this.setchose(this.current)
						this.setchose2(this.selectone.index,this.selectone.direct)
						this.setriskmsg()
						uni.stopPullDownRefresh()
					}
					
				})
			},
			sectionChange(index) {//选择全部或者单项
				this.current = index;
				this.setchose(index)
			},
			setchose(val){//更据类型选择显示产品
				this.productchose=[]
				if(val==0){
					this.productchose=this.products
					this.setchose2(this.selectone.index,this.selectone.direct)
					this.setriskmsg()
				}
				else{
					for(let i=0;i<this.products.length;i++){
						if(this.products[i].type==val){
							this.productchose.push(this.products[i])
						}
					}
					this.setchose2(this.selectone.index,this.selectone.direct)
					this.setriskmsg()
				}
			},
			setchose2(index,direct){//各种顺序排列产品
			    this.selectone.index=index
				this.selectone.direct=direct
			    let productchose1=this.productchose
				this.productchose=[]
				if(index==0){//收益率
					if(direct==1){//从小到大
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].rate>productchose1[j].rate){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
					else{//从大到小
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].rate<productchose1[j].rate){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
				}
				else if(index==1){//起购金额
					if(direct==1){
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].price>productchose1[j].price){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
					else{
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].price<productchose1[j].price){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
				}
				else if(index==2){//期限
					if(direct==1){
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].minday>productchose1[j].minday){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
					else{
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].minday<productchose1[j].minday){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
				}
				else if(index==3){//风险等级
					if(direct==1){
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].risk>productchose1[j].risk){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
					else{
						for(let i=0;i<productchose1.length;i++){
							for(let j=i+1;j<productchose1.length;j++){
								if(productchose1[i].risk<productchose1[j].risk){
									let center=productchose1[i]
									productchose1[i]=productchose1[j]
									productchose1[j]=center
								}
							}
						}
					}
				}
			    this.productchose=productchose1
				this.setriskmsg()
			},
			setriskmsg(){//设置风险信息
				this.risk=[]
				for(let i=0;i<this.productchose.length;i++){
					let risks={color:'success',text:''}
					if(this.productchose[i].risk==1){
						risks.text="风险极低"
					}
					else if(this.productchose[i].risk==2){
						risks.text="较低风险"
					}
					else if(this.productchose[i].risk==3){
						risks.color='primary'
						risks.text="中风险"
					}
					else if(this.productchose[i].risk==4){
						risks.color='warning'
						risks.text="较高风险"
					}
					else if(this.productchose[i].risk==5){
						risks.color='error'
						risks.text="高风险"
					}
					this.risk.push(risks)
				}
			},
		    intproduct(item){
				uni.setStorageSync('product',item)
				uni.navigateTo({
					url:"/pages/product/detail/detail"
				})
			}
		}
	}
</script>

<style lang="scss" >
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
.back{
	width: 100%;
	height: 100%;
	position: absolute;
	background-color: #fff;
}
</style>
