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
		this.width = attributes.width == undefined ? 1200 : attributes.width;
		this.textHeight = 150;
		this.gameWidth = this.width / 15;
		this.barWidth = this.gameWidth * .3;
		this.barPadding = this.gameWidth * .35;
		d3.json("data/teams/findStats?id=" + teamId, dojo.hitch(this, this.processData));
	},
	
	//	Callback function to be used when the xhr request for data returns
	processData: function(/*object*/json) {
		var params = this;
		this.data = json;
		this.yardScale = d3.scale.linear().
			domain([0, d3.max(params.data, function(datum) { return datum.rushingYards + datum.passingYards; })]).
			range([0, params.height]);
		this.visualize();
	},
	
	
	
	visualize: function() {
		var params = this;
		this.mainChart = d3.select("#" + this.divId).
			append("svg:svg").
			attr("width", this.width).
			attr("height", this.height + this.textHeight);
		
		this.mainChart.selectAll("rushYardBars").
			data(this.data).
			enter().
			append("svg:rect").
			attr("x", function(datum, index) { return index * params.gameWidth + params.barPadding; }).
			attr("y", function(datum) { return params.height - params.yardScale(datum.rushingYards + datum.passingYards); }).
			attr("width", this.barWidth).
			attr("height", function(datum) { return params.yardScale(datum.rushingYards); }).
			attr("fill", "#619BA3").
			on("mouseover", function(datum, index) { 
				d3.select(this.parentElement).
					append("svg:text.label").
					text(datum.rushingYards).
					attr("x", index * params.gameWidth + (params.gameWidth / 2)).
					attr("y", params.height - params.yardScale(datum.rushingYards + datum.passingYards)).
					attr("dy", "1.2em").
					attr("text-anchor", "middle").
					attr("fill", "black").
					attr("font-size", "14");
				}).
			on("mouseout", function(datum, index) {
				params.mainChart.selectAll(".label").remove();
			});
		
		this.mainChart.selectAll("passYardBars").
			data(this.data).
			enter().
			append("svg:rect").
			attr("x", function(datum, index) { return index * params.gameWidth + params.barPadding; }).
			attr("y", function(datum) { return params.height - params.yardScale(datum.passingYards); }).
			attr("width", this.barWidth).
			attr("height", function(datum) { return params.yardScale(datum.passingYards); }).
			attr("fill", "#BAA26E");
		
		this.mainChart.selectAll("yardTicks").
			data(params.yardScale.ticks(10)).
			enter().
			append("svg:line").
			attr("x1", 0).
			attr("x2", params.width).
			attr("y1", function(datum) { return params.yardScale(datum); }).
			attr("y2", function(datum) { return params.yardScale(datum); }).
			attr("stroke", "white").
			attr("stroke-width", "2");
		
		this.mainChart.selectAll("scoreText").
			data(this.data).
			enter().
			append("svg:text").
			attr("x", function(datum, index) { return index * params.gameWidth + (params.gameWidth / 2); }).
			attr("y", params.height).
			attr("dy", "1.2em").
			attr("text-anchor", "middle").
			text(function(datum) { return datum.awayScore + "-" + datum.homeScore; }).
			attr("fill", "#4A98A3").
			attr("font-size", "14");
		
		this.mainChart.selectAll("opponentText").
			data(this.data).
			enter().
			append("svg:text").
			attr("x", function(datum, index) { return index * params.gameWidth + (params.gameWidth / 2); }).
			attr("y", params.height + 18).
			attr("dy", "1.2em").
			attr("text-anchor", "middle").
			text(function(datum) { return datum.opponentName; }).
			attr("fill", "#193033").
			attr("font-size", "10");
		
		this.mainChart.selectAll("gameDateText").
			data(this.data).
			enter().
			append("svg:text").
			attr("x", function(datum, index) { return index * params.gameWidth + (params.gameWidth / 2); }).
			attr("y", params.height + 32).
			attr("dy", "1.2em").
			attr("text-anchor", "middle").
			text(function(datum) { return datum.gameDate; }).
			attr("fill", "#193033").
			attr("font-size", "10");
		
		
		
		
			
	}
});