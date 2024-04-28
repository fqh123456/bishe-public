<template>
	<view>
		<u-toast ref="uToast"></u-toast>
		<view>
			<u-sticky bgColor="#fff">
			   <u-subsection class="subsection" :list="list" mode="button" :current="current" @change="sectionChange"></u-subsection>
			</u-sticky>
			
			<view v-if="current==0">
				<u-empty v-if="userProductsnow.length==0" style="padding-top: 20%;" mode="search" text="暂无正在生效的理财产品" >
				</u-empty>
				<view v-for="(item,index) in userProductsnow" style="margin-top: 3%;">
					<view class="box"  >
						<view style="padding: 3%;">
							<view style="display: flex;">
								<view style="flex: 4;">
									<u--text prefixIcon="baidu" size="13" bold :text="item.name"></u--text>
									<u--text style="padding-top: 1%;"  size="10" type="info" :text="'购买时间：'+item.paytime"></u--text>
									<u--text v-if="item.rate>=0" style="padding-top: 1%;" size="10" type="success" :text="'当前利率：'+item.rate+'%'" ></u--text>
									<u--text v-if="item.rate<0" style="padding-top: 1%;" size="10" type="error" :text="'当前利率：'+item.rate+'%'" ></u--text>
								</view>
								<u--text @click="todetail(item)" size="16" prefixIcon="search" style="flex: 1;" type="primary" bold text="详情"></u--text>
							</view>
						    <u-divider  style="width: 100%"></u-divider>
							<view style="display: flex;">
								<u--text size="13" :text="'置入本金：￥'+item.cost"></u--text>
								<u--text size="13" :text="'现存金额：￥'+item.balance"></u--text>
							</view>
							<u--text size="13" type="info" v-if="item.days>=0" text="当前余额可取出"></u--text>
							<u--text size="10" type="info" v-if="item.days<0" :text="'剩余'+(-item.days)+'天可取出余额'"></u--text>
						</view>			  
					</view>	
				</view>	
			</view>	   
	
	        <view v-if="current==1">
				<u-empty v-if="userProductslast.length==0" style="padding-top: 20%;" mode="search" text="暂无失效的历史理财产品" >
				</u-empty>
	        	<view v-for="(item,index) in userProductslast" style="margin-top: 3%;">
	        		<view class="box"  >
	        			<view style="padding: 3%;">
							<view style="display: flex;">
								<u--text size="20" v-if="(item.balance-item.cost)>=0" type="success" :text="'收益：￥'+(item.balance-item.cost)"></u--text>
							    <u--text size="20" v-if="(item.balance-item.cost)<0" type="error" :text="'亏损：￥'+(item.balance-item.cost)"></u--text>
							    <u--text  align="right" style="padding-right: 5%;" @click="buy(item)" prefixIcon="shopping-cart" type="primary" text="再次购买" size="13" bold ></u--text>
							</view>
							<u-divider  style="width: 100%"></u-divider>
	        				<view style="display: flex;">
								<u--text prefixIcon="baidu" type="info" size="13" bold :text="item.name"></u--text>	
								
							</view>
							<view style="display: flex;">
								<u--text size="13" type="info" :text="'置入本金：￥'+item.cost"></u--text>
								<u--text size="13" type="info" :text="'取出金额：￥'+item.balance"></u--text>
							</view>		
							<u--text style="padding-top: 1%;"  size="10" type="info" :text="'购买时间：'+item.paytime"></u--text>
	        			</view>			  
	        		</view>	
	        	</view>	
	        </view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: ['当前理财', '历史理财'],
				current:0,
				bankcard:uni.getStorageSync('bankdetail'),
				userProducts:[],
				userProductsnow:[],
				userProductslast:[],
			};
		},
		onLoad(){
			this.getuserProducts()
		},
		methods:{
			sectionChange(index) {//选择储蓄或信用卡
							this.current = index;
						},
			getuserProducts(){//获取所有用户购买的产品
				this.request({
					url:"/UserProduct/getUserProducts",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
					}
				}).then(res=>{
					if(res.code==='200'){
						this.userProducts=res.data
						for(let i=0;i<this.userProducts.length;i++){
							if(this.userProducts[i].state==1){
								this.userProductsnow.push(this.userProducts[i])
							}
							else{
								this.userProductslast.push(this.userProducts[i])
							}
						}
					}
				})
			},
			todetail(item){
				this.request({
					url:"/Product/getProductOne",
					method:"POST",
					data:{
						id:item.productid,
					}
				}).then(res=>{
					if(res.code==='200'){
						uni.setStorageSync('product',res.data)
						uni.navigateTo({
							url:"/pages/product/detail/detail"
					})
				  }	
				})	
			},
			buy(item){
				let flag=0;
				for(let i=0;i<this.userProductsnow.length;i++){
					if(item.productid==this.userProductsnow[i].productid){
						flag=1
						this.$refs.uToast.show({
											type:'warning',
											duration:'2000',
											message:"您已购买该产品，请到当前理财板块查找",
										})
						break;
					}
				}
				if(flag==0){
					this.todetail(item)
				}
			}
		}
	}
</script>

<style lang="scss">
.box{
	margin: 1% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
</style>
