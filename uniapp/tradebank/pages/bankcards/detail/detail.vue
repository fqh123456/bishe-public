<template>
<<<<<<< HEAD
	<view>
		<u-sticky>
			<view class="box" >
				<view style="display: flex;">
					<u--text style="padding-top: 1%;padding-left: 3%;" align="left" type="info" :text="text"></u--text>
					<u--text @click="show=true" align="right" style="padding-top: 1%;padding-right: 3%" type="primary" prefixIcon="search" text="卡号详情" ></u--text>
				
				</view>
				<view style="padding-top:3%;padding-left: 3%">
					<u--text size="20" mode="price" :text="mycarddetail.balance"></u--text>
					<view style="display: flex;padding-bottom: 2%;">
						<u--text style="flex: 6;" size="13" suffixIcon="arrow-down-fill" :text="begintext" @click="timeshow1=true" ></u--text>
						<u--text style="flex: 6;" size="13" suffixIcon="arrow-down-fill" :text="endtext" @click="timeshow2=true" ></u--text>
						<u--text style="flex: 2;" align="left" size="13" text="确定" type="primary" @click="search()" ></u--text>
					</view>
				</view>
			</view>
		</u-sticky>
		<u-empty v-if="Detail.length==0" mode="search" text="所选时间内暂无收支记录" >
		</u-empty>
		<view v-if="Detail.length>0">
			<view style="width: 70%;margin: 0 auto;" v-for="(item,index) in Detail" >
				<u-divider ></u-divider>
					<u-cell  icon="rmb-circle" :title="'金额：'+item.cost" :value="item.type" :label="'交易时间：'+item.paytime" @click="openDetail(item)" >
					</u-cell>
			</view>
		</view>
		
		<u-popup :show="showDetail" customStyle="width:80%" mode="center" :round="8" @close="close" @open="open">
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
		
	<!-- 时间方面的控件======================================================================== -->
		<u-datetime-picker
		            @cancel="cancel1"
					@confirm="confirm1"
					title="开始时间"
					ref="datetimePicker"
					:show="timeshow1"
		            v-model="begindate"
					:minDate="mindate"
					:maxDate="nowdate"
					mode="date"
		        ></u-datetime-picker>
		<u-datetime-picker
		            @cancel="cancel2"
					@confirm="confirm2"
					title="结束时间"
					ref="datetimePicker"
					:show="timeshow2"
		            v-model="enddate"
					:minDate="begindate"
					:maxDate="nowdate"
					mode="date"
		        ></u-datetime-picker>
		<u-popup customStyle="width:60%" :round="8" mode="center" :show="show" @close="close">
		     <view >
		         <u--text style="padding-top: 5%;padding-bottom: 2%;"  align="center" :text="mycarddetail.cardnumber"></u--text>
			 </view>
=======
	<view>
		
		<u-sticky>
			<view class="box" >
				<view style="display: flex;">
					<u--text style="padding-top: 1%;padding-left: 3%;" align="left" type="info" :text="text"></u--text>
					<u--text @click="show=true" align="right" style="padding-top: 1%;padding-right: 3%" type="primary" prefixIcon="search" text="卡号详情" ></u--text>
				</view>
				<view style="padding-top:3%;padding-left: 3%">
					<view>
						<u--text  size="20" mode="price" :text="mycarddetail.balance"></u--text>
					</view>
					<view style="display: flex;padding-bottom: 2%;">
						<u--text style="flex: 6;" size="13" suffixIcon="arrow-down-fill" :text="begintext" @click="timeshow1=true" ></u--text>
						<u--text style="flex: 6;" size="13" suffixIcon="arrow-down-fill" :text="endtext" @click="timeshow2=true" ></u--text>
						<u--text style="flex: 2;" align="left" size="13" text="确定" type="primary" @click="search()" ></u--text>
					</view>
				</view>
			</view>
		</u-sticky>
		<u-loading-icon :show="showflag"></u-loading-icon>
		<u-empty v-if="Detail.length==0" mode="search" text="所选时间内暂无收支记录" >
		</u-empty>
		<view v-if="Detail.length>0">
			<view style="width: 70%;margin: 0 auto;" v-for="(item,index) in Detail" >
				<u-divider ></u-divider>
					<u-cell  icon="rmb-circle" :title="'金额：'+item.cost" :value="item.type" :label="'交易时间：'+item.paytime" @click="openDetail(item)" >
					</u-cell>
			</view>
		</view>
		
		<u-popup :show="showDetail" customStyle="width:80%" mode="center" :round="8" @close="close" @open="open">
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
		
	<!-- 时间方面的控件======================================================================== -->
		<u-datetime-picker
		            @cancel="cancel1"
					@confirm="confirm1"
					title="开始时间"
					ref="datetimePicker"
					:show="timeshow1"
		            v-model="begindate"
					:minDate="mindate"
					:maxDate="nowdate"
					mode="date"
		        ></u-datetime-picker>
		<u-datetime-picker
		            @cancel="cancel2"
					@confirm="confirm2"
					title="结束时间"
					ref="datetimePicker"
					:show="timeshow2"
		            v-model="enddate"
					:minDate="begindate"
					:maxDate="nowdate"
					mode="date"
		        ></u-datetime-picker>
		<u-popup customStyle="width:60%" :round="8" mode="center" :show="show" @close="close">
		     <view >
		         <u--text style="padding-top: 5%;padding-bottom: 2%;"  align="center" :text="mycarddetail.cardnumber"></u--text>
			 </view>
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
<<<<<<< HEAD
			return {
				mycarddetail:{},
				text:'',
				show:false,
				timeshow1:false,
				timeshow2:false,
				nowdate:'',//今天的时间（理论上也是maxdate）
				enddate:'',//选中的结束的时间
				begindate:'',//选择的开始时间
				mindate:'',//理论上最小选择的时间
				begintext:'',
				endtext:'',
				Detail:[],
				Onedetail:{},
				showDetail:false,
			};
		},
		onLoad(){
			this.getdate();
			////////////////////////
			this.search2();
			if(this.mycarddetail.type==1){
				this.text='龙卡通('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			else if(this.mycarddetail.type==2){
				this.text='个人养老账户('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			this.search()
		},
		methods:{
			getdate(){//获取各种需要的日期
				this.nowdate=Number(new Date());//今天
				this.enddate=Number(new Date());//选择的结束日期（默认为今天）
				
				var currentDate=new Date()
				currentDate.setMonth(currentDate.getMonth() - 1)
				this.begindate=Number(currentDate);//选择的开始日期(默认为上个月)
				
				var currentyear=new Date()
				
				currentyear.setFullYear(currentyear.getFullYear() - 2)
				this.mindate=Number(currentyear);//最小可选时间默认为两年前
				
				this.setTimetext()
			},
			setTimetext(){
				var date=new Date(this.begindate)
				var m=(date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
				var d=(date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
				this.begintext='开始时间:'+date.getFullYear()+'-'+m+'-'+d
				var date1=new Date(this.enddate)
				var m1=(date1.getMonth() + 1 < 10 ? '0' + (date1.getMonth() + 1) : date1.getMonth() + 1)
				var d1=(date1.getDate() < 10 ? '0' + (date1.getDate()) : date1.getDate())
				this.endtext='结束时间:'+date1.getFullYear()+'-'+m1+'-'+d1
			},
			close(){
				this.show=false
				this.showDetail=false
			},
			cancel1(){
				this.timeshow1=false
			},
			cancel2(){
				this.timeshow2=false
			},
			confirm1(e){
				this.begindate=e.value
				var date=new Date(this.begindate)
				var m=(date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
				var d=(date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
				this.begintext='开始时间:'+date.getFullYear()+'-'+m+'-'+d
				this.timeshow1=false
			},
			confirm2(e){
				this.enddate=e.value
				var date1=new Date(this.enddate)
				var m1=(date1.getMonth() + 1 < 10 ? '0' + (date1.getMonth() + 1) : date1.getMonth() + 1)
				var d1=(date1.getDate() < 10 ? '0' + (date1.getDate()) : date1.getDate())
				this.endtext='结束时间:'+date1.getFullYear()+'-'+m1+'-'+d1
				this.timeshow2=false
			},
			search(){
				this.request({
					url:"/BankCard/getCardsDetail",
					method:"POST",
					data:{
						cardsid:uni.getStorageSync('bankdetail').id,
						begintime:this.begindate,
						endtime:this.enddate
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
					}
					else{
						this.mycarddetail={}
					}
				})
			},
			openDetail(item){
				this.Onedetail=item;
				this.showDetail=true
			}
			
=======
			return {
				mycarddetail:{},
				text:'',
				show:false,
				timeshow1:false,
				timeshow2:false,
				nowdate:'',//今天的时间（理论上也是maxdate）
				enddate:'',//选中的结束的时间
				begindate:'',//选择的开始时间
				mindate:'',//理论上最小选择的时间
				begintext:'',
				endtext:'',
				Detail:[],
				Onedetail:{},
				showDetail:false,
				showflag:false
			};
		},
		onLoad(){
			this.showflag=true
			this.getdate();
			////////////////////////
			this.search2();
			if(this.mycarddetail.type==1){
				this.text='龙卡通('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			else if(this.mycarddetail.type==2){
				this.text='个人养老账户('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			else if(this.mycarddetail.type==3){
				this.text='信用卡('+this.mycarddetail.cardnumber.slice(-4)+')'
			}
			this.search()
			this.showflag=false
		},
		methods:{
			getdate(){//获取各种需要的日期
				this.nowdate=Number(new Date().setHours(0,0,0,0));//今天
				this.enddate=Number(new Date().setHours(23,59,59,0));//选择的结束日期（默认为今天）
				
				var currentDate=new Date(new Date().setHours(0,0,0,0))
				currentDate.setMonth(currentDate.getMonth() - 1)
				this.begindate=Number(currentDate);//选择的开始日期(默认为上个月)
				
				var currentyear=new Date()
				
				currentyear.setFullYear(currentyear.getFullYear() - 2)
				this.mindate=Number(currentyear);//最小可选时间默认为两年前
				
				this.setTimetext()
			},
			setTimetext(){
				var date=new Date(this.begindate)
				var m=(date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
				var d=(date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
				this.begintext='开始时间:'+date.getFullYear()+'-'+m+'-'+d
				var date1=new Date(this.enddate)
				var m1=(date1.getMonth() + 1 < 10 ? '0' + (date1.getMonth() + 1) : date1.getMonth() + 1)
				var d1=(date1.getDate() < 10 ? '0' + (date1.getDate()) : date1.getDate())
				this.endtext='结束时间:'+date1.getFullYear()+'-'+m1+'-'+d1
			},
			close(){
				this.show=false
				this.showDetail=false
			},
			cancel1(){
				this.timeshow1=false
			},
			cancel2(){
				this.timeshow2=false
			},
			confirm1(e){
				this.begindate=e.value
				var date=new Date(this.begindate)
				var m=(date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
				var d=(date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
				this.begintext='开始时间:'+date.getFullYear()+'-'+m+'-'+d
				this.timeshow1=false
			},
			confirm2(e){
				this.enddate=e.value
				var date1=new Date(this.enddate)
				var m1=(date1.getMonth() + 1 < 10 ? '0' + (date1.getMonth() + 1) : date1.getMonth() + 1)
				var d1=(date1.getDate() < 10 ? '0' + (date1.getDate()) : date1.getDate())
				this.endtext='结束时间:'+date1.getFullYear()+'-'+m1+'-'+d1
				this.timeshow2=false
			},
			search(){
				this.request({
					url:"/BankCard/getCardsDetail",
					method:"POST",
					data:{
						cardsid:uni.getStorageSync('bankdetail').id,
						begintime:this.begindate,
						endtime:this.enddate
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
			openDetail(item){
				this.Onedetail=item;
				this.showDetail=true
			}
			
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
		}
	}
</script>

<<<<<<< HEAD
<style lang="scss">
.box{
		margin: 5% auto;
		width: 95%;
		background-color: #fff;
		border: 1px solid #ddd;
		border-radius: 5px;
		box-shadow: 0 2px 5px rgba(0,0,0,0.1);
		background-image: url(~@/static/image/inset/background3.jpg);
		background-size: 100% 100%;
		background-position: 50% 50%;
		background-repeat: no-repeat;
	
=======
<style lang="scss">
.box{
		margin: 5% auto;
		width: 95%;
		background-color: #fff;
		border: 1px solid #ddd;
		border-radius: 5px;
		box-shadow: 0 2px 5px rgba(0,0,0,0.1);
		background-image: url(~@/static/image/inset/background3.jpg);
		background-size: 100% 100%;
		background-position: 50% 50%;
		background-repeat: no-repeat;
	
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
}
</style>
