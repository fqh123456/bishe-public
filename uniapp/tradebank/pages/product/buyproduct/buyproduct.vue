<template>
	<view>
<<<<<<< HEAD
		<u--text style="padding-top: 2%;" bold color="rgba(5,216,255)" align="center" size="20" :text="product.name"></u--text>
		<view class="box">
			<view style="padding: 3%;">
				<u--text bold text="购买金额" style="padding-bottom: 2%;"></u--text>
				<u--input
					    :placeholder="product.price+'元起购'"
					    prefixIcon="rmb"
					    prefixIconStyle="font-size: 22px;color: black"
						type="number"
						v-model="pay.cost"
						>
				</u--input>
				<u--text style="padding-top: 2%;" type="info" size="12" :text="'该产品购买金额超过'+(product.amount-product.sum)+'元后将会售罄'"></u--text>
				<u--text  type="info" size="12" :text="'预计'+(product.minday)+'天后将会计算收益'"></u--text>
			</view>
		</view>
		<view class="box" >
			<u-picker closeOnClickOverlay @cancel="show=false" @confirm="confirm" @close="show=false" :show="show" :columns="cards" keyName="card" ></u-picker>
			<view style="display: flex;padding: 3%;">
				<u--text style="flex: 2;" bold text="付款账户" ></u--text>
				<view  style="flex: 5;">
					<u--text bold size="16"  :text="cardtext" @click="show=true"></u--text>
					<u--text  type="info" size="10" :text="'账户活期余额￥'+balance"></u--text>
				</view>
				<u-icon style="flex: 1;" name="arrow-right" @click="show=true"></u-icon>
			</view>
		</view>
		<u-toast ref="uToast"></u-toast>
		<u-modal @close="show2=false" @cancel="show2=false" showCancelButton="true" @confirm="surebuy()" :show="show2"  :content="'确认购买当前产品吗？购买金额为￥'+pay.cost+'，购买账户为'+cardtext"></u-modal>
		<view style="width: 95%;margin: 0 auto;">
			<u-button type="primary" text="确认购买" @click="buy"></u-button>
=======
		<u--text style="padding-top: 2%;" bold color="rgba(5,216,255)" align="center" size="20" :text="product.name"></u--text>
		<view class="box">
			<view style="padding: 3%;">
				<u--text bold text="购买金额" style="padding-bottom: 2%;"></u--text>
				<u--input
					    :placeholder="product.price+'元起购'"
					    prefixIcon="rmb"
					    prefixIconStyle="font-size: 22px;color: black"
						type="number"
						v-model="pay.cost"
						>
				</u--input>
				<u--text style="padding-top: 2%;" type="info" size="12" :text="'该产品购买金额超过'+(product.amount-product.sum)+'元后将会售罄'"></u--text>
				<u--text  type="info" size="12" :text="'预计'+(product.minday)+'天后将会计算收益'"></u--text>
			</view>
		</view>
		<view class="box" >
			<u-picker closeOnClickOverlay @cancel="show=false" @confirm="confirm" @close="show=false" :show="show" :columns="cards" keyName="card" ></u-picker>
			<view style="display: flex;padding: 3%;">
				<u--text style="flex: 2;" bold text="付款账户" ></u--text>
				<view  style="flex: 5;">
					<u--text bold size="16"  :text="cardtext" @click="show=true"></u--text>
					<u--text  type="info" size="10" :text="'账户活期余额￥'+balance"></u--text>
				</view>
				<u-icon style="flex: 1;" name="arrow-right" @click="show=true"></u-icon>
			</view>
		</view>
		<u-toast ref="uToast"></u-toast>
		<u-modal @close="show2=false" @cancel="show2=false" showCancelButton="true" @confirm="surebuy()" :show="show2"  :content="'确认购买当前产品吗？购买金额为￥'+pay.cost+'，购买账户为'+cardtext"></u-modal>
		<view style="width: 95%;margin: 0 auto;">
			<u-button type="primary" text="确认购买" @click="buy"></u-button>
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
<<<<<<< HEAD
				product:uni.getStorageSync('product'),
				pay:{cost:'',cardid:''},
				show:false,
				cards:[[]],
				userCards:[],
				index:0,
				cardtext:'',
				balance:'',
				show2:false,
				
			};
		},
		onLoad() {
			this.getcards()
		},
		methods:{
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
			confirm(val){
				let index=val.value[0].index
				this.cardtext=this.cards[0][index].card
				this.balance=this.userCards[index].balance
				this.pay.cardid=this.userCards[index].cardid
				this.show=false
			},
			buy(){
				if(this.pay.cost==0||this.pay.cost==''||this.pay.cost==null){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额必须大于0！",
									})
				}
				else if(this.balance<this.pay.cost){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额不能大于当前账户的活期余额！",
									})
				}
				else if(this.balance<this.product.price){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额不能小于该产品的起购价！",
									})
				}
				else{
					this.show2=true
				}
			},
			surebuy(){
				this.request({
					url:"/Product/BuyProduct",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
						cost:this.pay.cost,
						cardid:this.pay.cardid,
						productid:this.product.id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.show2=false
						this.$refs.uToast.show({
											type:'success',
											duration:'2000',
											message:"购买成功！正在返回产品页面...",
											complete() {
												uni.navigateBack({
														url: "pages/product/detail/detail"
												})
											}
										})
					}
					else{
						this.show2=false
						this.$refs.uToast.show({
											type:'error',
											duration:'2000',
											message:res.msg,
										})
					}
				}) 
			}
=======
				product:uni.getStorageSync('product'),
				pay:{cost:'',cardid:''},
				show:false,
				cards:[[]],
				userCards:[],
				index:0,
				cardtext:'',
				balance:'',
				show2:false,
				
			};
		},
		onLoad() {
			this.getcards()
		},
		methods:{
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
			confirm(val){
				let index=val.value[0].index
				this.cardtext=this.cards[0][index].card
				this.balance=this.userCards[index].balance
				this.pay.cardid=this.userCards[index].cardid
				this.show=false
			},
			buy(){
				if(this.pay.cost==0||this.pay.cost==''||this.pay.cost==null){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额必须大于0！",
									})
				}
				else if(this.balance<this.pay.cost){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额不能大于当前账户的活期余额！",
									})
				}
				else if(this.balance<this.product.price){
					this.$refs.uToast.show({
										type:'warning',
										duration:'2000',
										message:"购买金额不能小于该产品的起购价！",
									})
				}
				else{
					this.show2=true
				}
			},
			surebuy(){//确认购买
				this.request({
					url:"/Product/BuyProduct",
					method:"POST",
					data:{
						uid:uni.getStorageSync('user').id,
						cost:this.pay.cost,
						cardid:this.pay.cardid,
						productid:this.product.id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.show2=false
						this.$refs.uToast.show({
											type:'success',
											duration:'2000',
											message:"购买成功！正在返回产品页面...",
											complete() {
												uni.navigateBack({
														url: "pages/product/detail/detail"
												})
											}
										})
					}
					else{
						this.show2=false
						this.$refs.uToast.show({
											type:'error',
											duration:'2000',
											message:res.msg,
										})
					}
					this.getcards()
				}) 
			}
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
		}
	}
</script>

<style lang="scss">
<<<<<<< HEAD
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
=======
.box{
	margin: 5% auto;
	width: 95%;
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
}
</style>
