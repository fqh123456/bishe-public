<template>
	<view>
		
		<view class="box">
			<view style="padding: 3%;">
				<u--text align="center" size="18" bold :text="product.name"></u--text>
				<u--text style="margin-top: 2%;" type="info" size="12" :text="'创建时间：'+product.createtime"></u--text>
				<view style="display: flex;">
					<u--text style="flex:3.2;" type="info" size="12" :text="product.rate+'% 利率'" suffixIcon="question-circle-fill" @click="checkrate()" ></u--text>
					<u--text v-if="product.type==1" style="flex: 5;" size="17" type="primary" text="固期产品"></u--text>
					<u--text v-if="product.type==2" style="flex: 5;" size="17" type="primary" text="限期产品"></u--text>
				</view>
				<view style="display: flex;margin-top: 2%;margin-bottom: 2%;">
					<u-tag icon="bookmark-fill" :text="'R'+product.risk+'风险'" plain size="mini" type="info"></u-tag>
					<u-tag style="padding-left: 2%;" icon="rmb-circle-fill" :text="product.price+'元起购'" plain size="mini" type="info"></u-tag>
					<u-tag v-if="product.type==1" style="padding-left: 2%;" icon="clock-fill" :text="'固期'+product.minday+'天'" plain size="mini" type="info"></u-tag>
				    <u-tag v-if="product.type==2" style="padding-left: 2%;" icon="clock-fill" :text="'最低持有'+product.minday+'天'" plain size="mini" type="info"></u-tag>
				</view>
				<view style="margin: 0 auto;margin-top: 1%;width: 90%;">
					 <u-collapse :value="['1']" >
						 <u-collapse-item
						       name=1
						       title="详情描述"
						     >
						       <text class="u-collapse-content">{{product.description}}</text>
						     </u-collapse-item>
					  </u-collapse>
				</view>
				<u--text style="padding-bottom: 2%;" color="rgba(245,136,0)" size="13" text="业绩比较基准不是预期收益率,不代表产品得未来表现和实际收益,不构成对产品收益的承诺"></u--text>
			</view>
		</view>
	
	    <view class="box">
			<view style="padding-top: 2%;padding-bottom: 2%;">
				<u--text style="padding-left: 2.5%;" type="info" size="12" text="总金额进度:" ></u--text>
				<u-line-progress style="width: 95%;margin: 0 auto;" :percentage="(product.sum/product.amount)*100" activeColor="rgba(178,214,250)"></u-line-progress>
			</view>
		</view>
		<u-sticky style="background-color: #fff;">
			<view style="display: flex;width: 95%;margin: 0 auto;">
				<u--text prefixIcon="question-circle-fill" size="13" style="flex: 1;" text="市场" @click="openmarket()"></u--text>
				<u-button style="flex: 5;" type="primary" text="购买" shape="circle" @click="buyproduct()"></u-button>
			</view>
		</u-sticky>
		<u-toast ref="uToast"></u-toast>
		<u-popup :show="show" @close="close">
		   <view style="padding: 5%;">
		       <u--text bold text="意向市场:"></u--text>
			   <view style="display: flex;">
				   <u--text v-for="(item,index) in marketNames" :text="item.marketname"></u--text>
			   </view>
		   </view>        
		</u-popup>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				product:uni.getStorageSync('product'),
				marketNames:[],
				show:false,
			};
		},
		onBackPress(){
			uni.removeStorageSync('product')
		},
		methods:{
			openmarket(){
				this.marketNames=[]
				this.request({
					url:"/MarketName/ProductToGetMarketName",
					method:"POST",
					data:{
						targetmarket:this.product.targetmarket
					}
				}).then(res=>{
					if(res.code==='200'){
						this.marketNames=res.data
						this.show=true
					}
					else{
						this.$refs.uToast.show({
											type:'warning',
											duration:'2000',
											message:res.msg,
										})
					}
					
				})
			},
			close(){
				this.show=false
			},
			buyproduct(){
				uni.navigateTo({
					url:"/pages/product/buyproduct/buyproduct"
				})
			},
			checkrate(){
				
			}
		}
	}
</script>

<style lang="scss">
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
</style>
