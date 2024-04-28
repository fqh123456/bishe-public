<<<<<<< HEAD
<template>
	<view >
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {
		},
		methods: {
			
		}
	}
</script>

<style>
	
=======
<template>
	<view >
		<u-button text="消费" @click="opneconsume()"></u-button>
		<u-popup customStyle="width: 80%;" :round="7" style="width: 80%;" mode="center" closeOnClickOverlay="true" :show="show" @close="close" @open="open">
		       <view style="padding: 5%;">
				   <u--form
				   				labelPosition="left"
				   				:model="model1"
				   				ref="uForm"
				   		>
				   			<u-form-item
							        labelWidth="30%"
				   					label="消费额度"
				   					borderBottom
				   					ref="item1"
				   			>
				   				<u--input
								        type="number"
				   						v-model="model1.cost"
				   						border="none"
				   				></u--input>
				   			</u-form-item>
				   			<u-form-item
				   					label="账户"
				   					borderBottom
				   					@click="showSex = true; hideKeyboard()"
				   					ref="item1"
				   			>
				   				<u--input
				   						v-model="model1.card.cardnumber"
				   						disabled
				   						disabledColor="#ffffff"
				   						placeholder="请选择账户"
				   						border="none"
				   				></u--input>
				   				<u-icon
				   						slot="right"
				   						name="arrow-right"
										@click="showSex=true"
				   				></u-icon>
				   			</u-form-item>
							<u-form-item
									borderBottom
									ref="item1"
							>
								<u--input
								        placeholder="请输入用途(选填)"
								        style="width: 80%;"
										v-model="model1.describe"
										border="none"
								></u--input>
							</u-form-item>
				   		</u--form>
						<u-action-sheet 
						                :closeOnClickOverlay="true" 
										:closeOnClickAction="true"
										:show="showSex"
										:actions="list"
										title="消费账户"
										description="推荐选择信用卡"
										@close="showSex = false"
										@select="selectClick"
								>
					</u-action-sheet>
					<u-button style="margin-top: 5%;" text="确定" @click="consume()"></u-button>
			   </view>
			   <u-toast ref="uToast2"></u-toast>
		</u-popup>
		<u-toast ref="uToast"></u-toast>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				mycards:[],
				show:false,
				model1:{cost:'',card:{cid:'',cardnumber:''},describe:''},
				showSex:false,
				list:[],
			}
		},
		onLoad() {
			
		},
		onShow(){
			this.getCards()
		},
		methods: {
			opneconsume(){//弹出消费框
				this.show=true
			},
			consume(){//消费
				if(this.model1.cost<=0){
					this.$refs.uToast2.show({
										type:'error',
										duration:'1500',
										message:"消费金额必须大于0",
									})
				}
				else{
					this.request({
						url:"/Pay/consume",
						method:"POST",
						data:{
							cid:this.model1.card.cid,
							cost:this.model1.cost,
							describe:this.model1.describe
						}
					}).then(res=>{
						
						if(res.code==='200'){
							this.show=false
							this.$refs.uToast.show({
												type:'success',
												duration:'1500',
												message:"消费成功！",
											})
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
			},
			getCards(){//获取卡号以及银行卡类型等元素的操作
			    this.list=[]
				this.request({
					url:"/BankCard/getCards",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
						cards:uni.getStorageSync('user').bankcards
					}
				}).then(res=>{
					if(res.code==='200'){
						this.mycards=res.data
						for(let i=0;i<this.mycards.length;i++){
							let card={name:'',subname:'',cid:'',color:"black"}
							card.cid=this.mycards[i].id
							if(this.mycards[i].type!=3){
								card.name="储蓄卡("+this.mycards[i].cardnumber.slice(-4)+")"
								card.subname="余额："+this.mycards[i].balance
								this.list.push(card)
							}
							else{
								card.name="信用卡("+this.mycards[i].cardnumber.slice(-4)+")"
								card.color="rgba(97,175,239)"
								card.subname="剩余额度："+this.mycards[i].balance
								this.list.unshift(card)
							}
						}
					}
				})
			},
		    close(){
				this.show=false
			},
			selectClick(index){
				this.model1.card.cardnumber=index.name
				this.model1.card.cid=index.cid
			}
		}
	}
</script>

<style>
	
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
</style>
