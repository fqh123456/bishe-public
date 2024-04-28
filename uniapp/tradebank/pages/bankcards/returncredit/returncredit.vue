<template>
	<view>
		<u-sticky bgColor="#fff">
		   <u-subsection class="subsection" :list="list" mode="button" :current="current" @change="sectionChange"></u-subsection>
		</u-sticky>
		
		
		<view v-if="current==0">
			<view v-if="needreturndbills.length>0" v-for="(item,index) in needreturndbills">
				<view class="box" @click="intoreturn(item)" >
					<view style="padding: 2%;" >
						<view >
							<u--text size="17" type="error" bold align="center" :text="'欠款额度：￥'+(item.debt.needreturn+item.debt.interest).toFixed(2)"></u--text>
						</view>
						<u--text size="13"  type="info" :text="'其中本金为￥'+item.debt.needreturn.toFixed(2)+'，利息为￥'+item.debt.interest.toFixed(2)"></u--text>
						<u-divider ></u-divider>
						<u--text bold style="flex: 2;" :text="'账单生成时间：'+item.debt.time"></u--text>
						<view style="display: flex;">
							<u--text style="flex: 2;" :text="'逾期时间：'+(item.debt.timelast)"></u--text>
							<u--text style="flex: 1;" size="13" type="primary" v-if="item.debt.days==0" text="尚未逾期"></u--text>
							<u--text style="flex: 1;" size="13" type="error" v-if="item.debt.days>0" :text="'已逾期'+item.debt.days+'天'"></u--text>
						</view>
						<u--text size="13" type="info" :text="'总消费：￥'+item.debt.cost.toFixed(2)"></u--text>
						<u--text type="success":text="'已还款：￥'+(item.debt.returnmoney).toFixed(2)"></u--text>
						
					</view>
					
				</view>
			</view>
			<u-empty v-if="needreturndbills.length==0" style="padding-top: 20%;" mode="search" text="暂无未还清的账单" >
			</u-empty>
				
		</view>
		
		
		<view v-if="current==1">
			<view v-if="returnedbills.length>0" v-for="(item,index) in returnedbills">
				<view class="box">
					<view style="padding: 2%;">
						<view style="padding: 2%;" >
							<view >
								<u--text size="17" type="error" bold align="center" :text="'共还款：￥'+(item.debt.returnmoney).toFixed(2)"></u--text>
							</view>
							<u--text size="13"  type="info" :text="'其中本金为￥'+item.debt.cost.toFixed(2)+'，利息为￥'+(item.debt.returnmoney-item.debt.cost).toFixed(2)"></u--text>
							<u-divider ></u-divider>
							<u--text bold style="flex: 2;" :text="'账单生成时间：'+item.debt.time"></u--text>
							<view style="display: flex;">
								<u--text style="flex: 2;" :text="'设定逾期时间：'+(item.debt.timelast)"></u--text>
								<u--text style="flex: 1;" size="13" type="primary" v-if="item.debt.days==0" text="该账单未逾期"></u--text>
								<u--text style="flex: 1;" size="13" type="error" v-if="item.debt.days>0" :text="'共逾期'+item.debt.days+'天'"></u--text>
							</view>
						</view>
					</view>
				</view>
			</view>
			<u-empty v-if="returnedbills.length==0" style="padding-top: 20%;" mode="search" text="暂无已还清的账单" >
			</u-empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				cardsdetails:uni.getStorageSync('bankdetail'),
				bills:[],
				returnedbills:[],
				needreturndbills:[],
			    list: ['未还清账单', '已还清账单'],
			    current:0,
				
			};
		},
		onLoad(){
			
		},
		onShow(){
			this.getbills()
		},
		methods:{
			sectionChange(index) {//选择储蓄或信用卡
							this.current = index;
						},
			getbills(){
				this.returnedbills=[]
				this.bills=[]
				this.needreturndbills=[]
				this.request({
					url:"/CreditCard/getBills",
					method:"POST",
					data:{
						cid:uni.getStorageSync('bankdetail').id,
					}
				}).then(res=>{
					if(res.code==='200'){
						this.bills=res.data
						for(let i=0;i<this.bills.length;i++){
							if(this.bills[i].debt.needreturn<0.01){
								this.returnedbills.push(this.bills[i])
							}
							else{
								this.needreturndbills.push(this.bills[i])
							}
						}
						console.log(this.needreturndbills)
					}
					
				})
			},
			intoreturn(item){//进入账单详情页
				uni.setStorageSync('CreditCardDetails',item)
				uni.navigateTo({
					url:"/pages/bankcards/returncredit/returnDetailCredit/returnDetailCredit"
				})
			},
			
		}
	}
</script>

<style lang="scss">
.box{
		margin: 2% auto;
		width: 95%;
		background-color: #fff;
		border: 1px solid #ddd;
		border-radius: 5px;
		box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	
}
</style>
