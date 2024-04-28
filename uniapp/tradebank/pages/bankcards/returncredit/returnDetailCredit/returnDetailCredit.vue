<template>
	<view>
		<u-sticky bgColor="#fff">
		   <view class="box">
		   	<view style="padding: 5%;">
		   			<view >
		   				<u--text size="17" type="error" bold align="center" :text="'欠款额度：￥'+(item.debt.needreturn+item.debt.interest).toFixed(2)"></u--text>
		   			</view>
		   			<u--text size="13"  type="info" :text="'其中本金为￥'+item.debt.needreturn.toFixed(2)+'，利息为￥'+item.debt.interest.toFixed(2)"></u--text>
					<u-divider ></u-divider>
					<u--form
									labelPosition="left"
									ref="uForm"
							>   
							    <u-picker closeOnClickOverlay @cancel="show=false" @confirm="confirm" @close="show=false" :show="show" :columns="cards" keyName="card" ></u-picker>
							    <view style="display: flex">
							    	<u--text style="flex: 2;" bold text="付款账户" ></u--text>
							    	<view  style="flex: 5;">
							    		<u--text bold size="16"  :text="cardtext" @click="show=true"></u--text>
							    		<u--text  type="info" size="10" :text="'账户活期余额￥'+balance"></u--text>
							    	</view>
							    	<u-icon style="flex: 1;" name="arrow-right" @click="show=true"></u-icon>
							    </view>
								<u-form-item
								        label-width="25%"
										label="还款 (元) :"
										borderBottom
										ref="item1"
								>
									<u--input
									        
									         :placeholder="'还需还款￥'+(item.debt.needreturn+item.debt.interest).toFixed(2)"
									        type="number"
											v-model="pay.cost"
											border="none"
									>
									    <template slot="suffix">
									    	<u--text @click="input((item.debt.needreturn+item.debt.interest).toFixed(2))" type="primary" size="12" text="一键填入"></u--text>				
									    </template>
									</u--input>
								</u-form-item>
							</u--form>
					<u--text size="13"  type="info" :text="'信用卡号：'+mycarddetail.cardnumber"></u--text>
		   			<view style="display: flex;">
		   				<u--text style="flex: 2;" :text="'逾期时间：'+(item.debt.timelast)"></u--text>
		   				<u--text align="right" style="flex: 1;" size="13" type="primary" v-if="item.debt.days==0" text="尚未逾期"></u--text>
		   				<u--text style="flex: 1;" size="13" type="error" v-if="item.debt.days>0" :text="'已逾期'+item.debt.days+'天'"></u--text>
		   			</view>
		   			<u--text type="success":text="'已还款：￥'+(item.debt.returnmoney).toFixed(2)"></u--text>
		   	</view>
		   </view>
		</u-sticky>
		<view v-if="Detail.length>0" style="padding-bottom: 15%;">
			<view style="width: 70%;margin: 0 auto" v-for="(items,index) in Detail" >
				<u-divider ></u-divider>
					<u-cell  icon="rmb-circle" :title="'金额：'+items.cost" :value="items.type" :label="'交易时间：'+items.paytime" @click="openDetail(items)" >
					</u-cell>
			</view>
		</view>
		<view style="bottom: 0;position: fixed;width: 100%;">
			<u-button type="primary" text="确认还款" @click="openrturn" ></u-button>
		</view>
		<u-toast ref="uToast"></u-toast>
<!-- 账单弹出层===================================================== -->
		<u-popup :show="showDetail" customStyle="width:80%" mode="center" :round="8" @close="close">
			
		     <view style="width: 90%;margin: 0 auto;padding-top: 5%;">
				 <u--text size="17" align="center" :text="Onedetail.type"></u--text>
		         <u--text size="20" align="center" bold mode="price" :text="Onedetail.cost"></u--text>
				 <view style="display: flex;padding-bottom: 2%;padding-top: 5%;">
				 					 <u--text type="info" text="账单编号"></u--text>
				 					 <u--text :text="Onedetail.id"></u--text>
				 </view>
				 <view style="display: flex;padding-bottom: 2%;padding-top: 2%;">
				 					 <u--text style="flex: 4;" type="info" text="交易时间"></u--text>
				 					 <u--text style="flex: 5;" :text="Onedetail.paytime"></u--text>
				 </view>
				 <view style="display: flex;padding-bottom: 2%;padding-top: 2%;">
					 <u--text type="info" text="交易账户"></u--text>
					 <u--text :text="mycarddetail.cardnumber"></u--text>
				 </view>
				 <view v-if="Onedetail.reciaccount!=null" style="display: flex;padding-bottom: 2%;padding-top: 2%;">
				 					 <u--text type="info" text="交易对象"></u--text>
				 					 <u--text :text="Onedetail.reciaccount"></u--text>
				 </view>
				 <view v-if="Onedetail.describe!=''&&Onedetail.describe!=null" style="display: flex;padding-bottom: 2%;padding-top: 2%;">
				 					 <u--text type="info" text="交易用途(备注)"></u--text>
				 					 <u--text :text="Onedetail.describe"></u--text>
				 </view>
				 <view style="display: flex;padding-bottom: 8%;padding-top: 2%;">
				 					 <u--text type="info" text="交易地点"></u--text>
				 					 <u--text :text="Onedetail.place"></u--text>
				 </view>
		     </view>      
		</u-popup>
		<u-popup :show="showreturn" customStyle="width:80%" mode="center" :round="8" @close="close">
			<u-toast ref="uToast2"></u-toast>
		     <view style="width: 90%;margin: 0 auto;padding-top: 5%;padding-bottom: 5%;">
				 <u--text size="14" :text="'确定使用账户：'+cardtext+'进行还款操作？还款金额为：￥'+pay.cost"></u--text>
				 <u--input style="margin-top: 2%;" type="password"  placeholder="请输入该账号的密码" v-model="password"></u--input>
				 <u-button type="primary" text="确定" style="margin-top: 2%;" @click="surereturn"></u-button>
		     </view>      
		</u-popup>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				item:uni.getStorageSync('CreditCardDetails'),
				mycarddetail:{},
				returnm:'',
				cards:[[]],
				show:false,
				userCards:[],
				showDetail:false,
				showreturn:false,
				Detail:[],
				Onedetail:{},
				pay:{cost:'',cardid:''},
				cardtext:'',
				balance:'',
				password:'',
			};
		},
		onBackPress() {
			uni.removeStorageSync('CreditCardDetails')
		},
		onLoad() {
			this.searchBills()
			this.search2()
			if(this.mycarddetail.type==1){
				this.text='龙卡通('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			else if(this.mycarddetail.type==2){
				this.text='个人养老账户('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			else if(this.mycarddetail.type==3){
				this.text='信用卡('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			this.getcards()
		},
		methods:{
			confirm(val){
				let index=val.value[0].index
				this.cardtext=this.cards[0][index].card
				this.balance=this.userCards[index].balance
				this.pay.cardid=this.userCards[index].cardid
				this.show=false
			},
		    searchBills(){
		    	this.request({
		    		url:"/BankCard/getCardsDetailbyids",
		    		method:"POST",
		    		data:{
		    			ids:uni.getStorageSync('CreditCardDetails').bills,
						cid:uni.getStorageSync('bankdetail').id
		    		}
		    	}).then(res=>{
		    		if(res.code==='200'){
		    			this.Detail=res.data
		    		}
		    		else{
		    			this.Detail=[]
		    		}
		    	})
		    },
			openDetail(item){
				this.Onedetail=item;
				this.showDetail=true
			},
			close(){
				this.showDetail=false
				this.show=false
				this.showreturn=false
			},
			getcards(){
				this.request({
					url:"/BankCard/getCards",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					if(res.code==='200'){
						let index=0;
						for(let i=0;i<res.data.length;i++){
							if(res.data[i].type==1||res.data[i].type==2){
								this.userCards.push(res.data[i])
								let len=res.data[i].cardnumber.length
								let nm=res.data[i].cardnumber.slice(0,4)+"****"+res.data[i].cardnumber.slice(len-4,len)
								let nnm={card:'',cardid:'',index:''}
								nnm.card=nm
								nnm.cardid=res.data[i].id
								nnm.index=index
								this.cards[0].push(nnm)
								index++
							}
						}
						this.cardtext=this.cards[0][0].card
						this.balance=this.userCards[0].balance
						this.pay.cardid=this.userCards[0].id
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
			search2(){
				this.request({
					url:"/BankCard/getCardsDetail2",
					method:"POST",
					data:{
						cardsid:uni.getStorageSync('bankdetail').id,
					}
				}).then(res=>{
					if(res.code==='200'){
						this.mycarddetail=res.data
						console.log(this.mycarddetail)
					}
					else{
						this.mycarddetail={}
					}
				})
			},
			input(val){
				if(this.balance>=val){
					this.pay.cost=val
				}
				else{
					this.pay.cost=this.balance
				}
			},
			openrturn(){//打开还款面板
			    if(this.pay.cost>0){
					this.showreturn=true
				}
				else{
					this.$refs.uToast.show({
										type:'warning',
										duration:'1500',
										message:"还款金额必须大于0！",
									})
				}
			},
			surereturn(){//确定还款
				if(this.password==uni.getStorageSync('user').password){
					this.request({
						url:"/Pay/returnCredit",
						method:"POST",
						data:{
							cost:this.pay.cost,
							pcid:this.pay.cardid,
							ccid:uni.getStorageSync('bankdetail').id,
							id:uni.getStorageSync('CreditCardDetails').id
						}
					}).then(res=>{
						if(res.code==='200'){
							this.showreturn=false
							this.$refs.uToast.show({
												type:'success',
												duration:'1500',
												message:"还款成功",
											})
							this.getCreditCardDetails()
							this.pay.cost=''
						}
						else{
							this.$refs.uToast2.show({
												type:'error',
												duration:'1500',
												message:res.msg,
											})
						}
					})
				}
				else{
					this.$refs.uToast2.show({
										type:'error',
										duration:'1500',
										message:"账号密码错误！",
									})
					this.password=''
				}
			},
			getCreditCardDetails(){
				this.request({
					url:"/CreditCard/getBill",
					method:"POST",
					data:{
						cid:uni.getStorageSync('bankdetail').id,
						id:uni.getStorageSync('CreditCardDetails').id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.item=res.data
						uni.setStorageSync('CreditCardDetails',res.data)
					}
				})
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
