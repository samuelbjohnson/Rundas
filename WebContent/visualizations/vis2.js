dojo.declare("vis2", null, {
	
	//	Accepts the NCAA team id for the team whose season is to be displayed, as well
	//	as the id of the html container to be used to display it.
	//	attributes can optionally define the following:
	//		height: the height (in pixels) of the chart
	//		width: the width (in pixels) of the chart
	constructor: function(/*numeric*/teamId, /*string*/divId, /*object*/attributes) {
		if (attributes == null) attributes = {};
		this.divId = divId;
		this.height = attributes.height == undefined ? 500 : attributes.height;
		this.width = attributes.width == undefined ? 900 : attributes.width;
		this.gameWidth = this.width / 15;
		this.barWidth = this.gameWidth * .4;
		this.barPadding = this.gameWidth * .3;
		d3.json("data/teams/findStats?id=" + teamId, dojo.hitch(this, this.processData));
	},
	
	//	Callback function to be used when the xhr request for data returns
	processData: function(/*object*/json) {
		this.data = json;
		this.yardScale = d3.scale.linear().
			domain([0, d3.max(this.data, function(datum) { return datum.rushingYards + datum.passingYards; })]).
			rangeRound([0, this.height]);
		this.visualize();
	},
	
	visualize: function() {
		var params = this;
		this.mainChart = d3.select("#" + this.divId).
			append("svg:svg").
			attr("width", this.width).
			attr("height", this.height);
		
		this.mainChart.selectAll("rushYardBars").
			data(this.data).
			enter().
			append("svg:rect").
			attr("x", function(datum, index) { return index * params.gameWidth + params.barPadding; }).
			attr("y", function(datum) { return params.height - params.yardScale(datum.rushingYards + datum.passingYards); }).
			attr("width", this.barWidth).
			attr("height", function(datum) { return params.yardScale(datum.rushingYards); }).
			attr("fill", "#55dfff");
		
		this.mainChart.selectAll("passYardBars").
			data(this.data).
			enter().
			append("svg:rect").
			attr("x", function(datum, index) { return index * params.gameWidth + params.barPadding; }).
			attr("y", function(datum) { return params.height - params.yardScale(datum.passingYards); }).
			attr("width", this.barWidth).
			attr("height", function(datum) { return params.yardScale(datum.passingYards); }).
			attr("fill", "#ffa750");
	}
});