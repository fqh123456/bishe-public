<template>
<<<<<<< HEAD
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
		
=======
	<view>
		<view >
			<view class="box" v-if="userProduct.productid!=null">
				<view style="padding: 2.5%;;">
					<view style="display: flex;">
						<u--text style="flex: 1;" prefixIcon="baidu" bold text="理财情况"></u--text>
						<u--text style="flex: 3;" size="13" type="info" text="(您已购买当前产品)"></u--text>
					</view>
					<u--text size="12" type="info" text="当前已购买本产品并且正在产生效益"></u--text>
					<u--text size="15" :text="'购买时间：'+userProduct.paytime"></u--text>
					<view style="display: flex;padding-top: 2%;padding-bottom: 2%;">
						<u--text size="20" bold :text="'余额：￥'+userProduct.balance.toFixed(2)"></u--text>
						<u--text size="15"  :text="'本金：￥'+userProduct.cost.toFixed(2)"></u--text>
					</view>
					<u--text type="success" v-if="(userProduct.balance-userProduct.cost>=0)" :text="'盈利：￥'+(userProduct.balance-userProduct.cost).toFixed(2)" > </u--text>
					<u--text type="error" v-if="(userProduct.balance-userProduct.cost<0)" :text="'亏损：￥'+(userProduct.balance-userProduct.cost).toFixed(2)" > </u--text>
				</view>
			</view>
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
					<u--text style="padding-left: 2.5%;" type="info" size="12" :text="'总金额进度:('+product.sum+'/'+product.amount+')'" ></u--text>
					<u-line-progress style="width: 95%;margin: 0 auto;" :percentage="(product.sum/product.amount)*100" activeColor="rgba(178,214,250)"></u-line-progress>
				</view>
			</view>
		
			<u-sticky style="background-color: #fff;">
				<view style="display: flex;width: 95%;margin: 0 auto;">
					<u--text prefixIcon="question-circle-fill" size="13" style="flex: 1;" text="市场" @click="openmarket()"></u--text>
					<u-button v-if="userProduct.productid==null" style="flex: 5;" type="primary" text="购买" shape="circle" @click="buyproduct()"></u-button>
					<view v-if="userProduct.productid!=null" style="display: flex;flex: 5;">
						<u-button style="flex: 1;margin-right: 5%"   plain type="primary" text="取出" shape="circle" @click="outmoney()"></u-button>
						<u-button style="flex: 3;padding-left: 5%;"  type="success" text="加购" shape="circle" @click="addbuy()"></u-button>
					</view>
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
			
			<u-popup :show="show4"  @close="close">
			   <view style="padding: 5%;">
			       <u--text bold text="历史利率:"></u--text>
				   <view v-for="(item,index) in historyrate" style="padding-bottom: 2%;display: flex;"  >
					   <u--text style="flex: 3;" :text="'时间：'+item.time"></u--text>
					   <u--text style="flex: 2;" :text="'利率:'+item.rate+'%'"></u--text>
				   </view>
			   </view>        
			</u-popup>
			
		</view>
		<u-popup customStyle="width:80%" :round="7" mode="center" closeOnClickOverlay=true :show="show2" @close="close" @open="open">
		    <view style="padding: 5%;">
				<u--text size="13" :text="'加购本产品将使用账号：'+card.cardnumber+'进行购买，该账号余额为：￥'+card.balance"></u--text>
				<view style="width: 90%;padding-top: 2%;margin: 0 auto;">
					<u--input type="number" placeholder="加购金额" v-model="addmoney"></u--input>
				</view>	
				<view style="width: 90%;padding-top: 2%;margin: 0 auto;">
					<u--input type="password" placeholder="请输入您的账号密码" v-model="password"></u--input>
				</view>
				<view style="padding-top: 5%;">
					<u-button  type="primary"  text="确定" @click="sureadd()"></u-button>
				</view>
				<u-toast ref="uToast2"></u-toast>
			</view>
		</u-popup>
		<u-popup customStyle="width:80%" :round="7" mode="center" closeOnClickOverlay=true :show="show5" @close="close" @open="open">
		    <view style="padding: 5%;">
				<u--text size="13" :text="'是否确认取出该产品的剩余额度？剩余额度：￥'+userProduct.balance+'，取出后将全部转存到购买账户：'+card.cardnumber+'中'"></u--text>
				<view style="width: 90%;padding-top: 2%;margin: 0 auto;">
					<u--input type="password" placeholder="请输入您的账号密码" v-model="password"></u--input>
				</view>
				<view style="padding-top: 5%;">
					<u-button  type="primary"  text="确定" @click="sureout()"></u-button>
				</view>
				<u-toast ref="uToast2"></u-toast>
			</view>
		</u-popup>
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
	</view>
</template>

<script>
	export default {
		data() {
			return {
<<<<<<< HEAD
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
=======
				product:uni.getStorageSync('product'),
				marketNames:[],
				show:false,
				show2:false,
				show3:false,
				show4:false,
				show5:false,
				userProduct:{productid:null},
				card:{},
				addmoney:"",
				open:['1'],
				password:"",
				historyrate:[],
			};
		},
		onBackPress(){
			uni.removeStorageSync('product')
		},
		onLoad(){
			this.getproduct()
			this.getUserProduct()
		},
		onShow(){
			this.getproduct()
			this.getUserProduct()
		},
		methods:{
			getproduct(){
				this.request({
					url:"/Product/getProductOne",
					method:"POST",
					data:{
						id:this.product.id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.product=res.data
						uni.setStorageSync('product',res.data)
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
				this.show2=false
				this.show3=false
				this.show5=false
				this.show4=false
			},
			buyproduct(){//跳转至购买界面
				uni.navigateTo({
					url:"/pages/product/buyproduct/buyproduct"
				})
			},
			checkrate(){//查看历史利率
				this.show4=true
				this.gethistoryrate()
			},
			gethistoryrate(){//获取历史利率
				this.request({
					url:"/UserProduct/gethistoryrate",
					method:"POST",
					data:{
						pid:this.product.id,
					}
				}).then(res=>{
					if(res.code==='200'){	
						this.historyrate=res.data
					}
				})
			},
			sureadd(){//确认加购
			    if(this.password!=uni.getStorageSync('user').password){
					this.$refs.uToast2.show({
										type:'error',
										duration:'1500',
										message:"账号密码错误！",
									})
				}
				else{
					if(this.addmoney==''||this.addmoney<=0){
						this.$refs.uToast2.show({
											type:'warning',
											duration:'1500',
											message:"购买金额必须大于0！",
										})
					}
					else{
						if(this.addmoney>this.card.balance){
							this.$refs.uToast2.show({
												type:'warning',
												duration:'1500',
												message:"购买金额必须小于当前账户余额！",
											})
						}
						else{
							this.request({
								url:"/UserProduct/addBuyUserProduct",
								method:"POST",
								data:{
									pid:this.product.id,
									uid:uni.getStorageSync('user').id,
									cardid:this.card.id,
									add:this.addmoney
								}
							}).then(res=>{
								if(res.code==='200'){
									this.show2=false
									this.$refs.uToast.show({
														type:'success',
														duration:'1500',
														message:"加购成功！",
													})
									this.addmoney=""
									this.getproduct()
									this.getUserProduct()
									this.getcard()
								}
								else{
									this.show2=false
									this.addmoney=""
									this.getUserProduct()
									this.getcard()
									this.$refs.uToast.show({
														type:'error',
														duration:'1500',
														message:res.msg,
													})
								}
							})
						}
					}
				}
				this.password=""
			},
			sureout(){//确认取出
				if(this.password!=uni.getStorageSync('user').password){
					this.$refs.uToast2.show({
										type:'error',
										duration:'1500',
										message:"账号密码错误！",
									})
					
				}
				else{
					this.request({
						url:"/UserProduct/outProduct",
						method:"POST",
						data:{
							pid:this.product.id,
							uid:uni.getStorageSync('user').id,
						}
					}).then(res=>{
						if(res.code==='200'){
							this.$refs.uToast.show({
												type:'success',
												duration:'1500',
												message:"取出余额成功！将跳转到产品界面",
												complete() {
													uni.navigateBack({
															url: "pages/product/product"
													})
												}
											})	
						}
						else{
							this.$refs.uToast.show({
												type:'error',
												duration:'1500',
												message:res.msg,
											})
						}
						this.show5=false
					})
				}
				this.password=""
			},
			outmoney(){//打开取出面板
				this.show5=true
			},
			getcard(){//获取购买当前产品的银行卡信息
				this.card={}
				this.request({
					url:"/BankCard/getCard",
					method:"POST",
					data:{
						cardid:this.userProduct.cardid
					}
				}).then(res=>{
					if(res.code==='200'){
						this.card=res.data
					}
				})
			},
			getUserProduct(){//获取用户购买产品信息
			    this.userProduct={productid:null}
				this.request({
					url:"/UserProduct/getUserProduct",
					method:"POST",
					data:{
						pid:this.product.id,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.userProduct=res.data
						this.getcard()
					}
				})
			},
		    addbuy(){//加购
				this.show2=true
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
