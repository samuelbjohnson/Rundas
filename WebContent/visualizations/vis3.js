dojo.declare("vis3", null, {
	constructor: function(/*string*/ divId) {
		this.resultCount = 0;
		this.divId = divId;
		d3.json("data/teams/findConferenceTeams", dojo.hitch(this, this.processConferenceTeams));
	},
	
	processConferenceTeams: function(json) {
		var indexByTeamId = {}, teamByIndex = {}, index = 0, matrix = [], that = this, i;
		json.forEach(function(team) {
			indexByTeamId[team.teamId] = index;
			teamByIndex[index] = team;
			matrix[index] = [];
			index++;
		});
		this.indexByTeamId = indexByTeamId;
		this.teamByIndex = teamByIndex;
		this.matrix = matrix;
		this.teamCount = index;
		for(i = 0; i < index; i++) {
			d3.json("data/teams/findStats?id=" + teamByIndex[i].teamId, function(json) {
				json.forEach(function(game) {
					if(that.indexByTeamId[game.opponentId] != undefined) {
						that.matrix[indexByTeamId[game.teamId]][indexByTeamId[game.opponentId]] = 
							game.atHome ? game.awayScore : game.homeScore;
					}
				});
				that.reportResult();
			});
		}
	},
	
	reportResult: function() {
		this.resultCount++;
		if (this.resultCount >= this.teamCount) {
			this.finalizeMatrix();
		}
	},
	
	finalizeMatrix: function() {
		var i, j;
		for(i = 0; i < this.teamCount; i++) {
			for(j = 0; j < this.teamCount; j++) {
				if(this.matrix[i][j] == undefined) {
					this.matrix[i][j] = 0;
				}
			}
		}
		this.visualize();
	},
	
	visualize: function() {
		var that = this;
		var r1 = 960 / 2;
		var r0 = r1 - 120;
		
		var fill = d3.scale.category20c();
		
		var chord = d3.layout.chord().
			padding(.04).
			sortSubgroups(d3.descending).
			sortChords(d3.descending);
		
		var arc = d3.svg.arc().
			innerRadius(r0).
			outerRadius(r0 + 20);
		
		var svg = d3.select("#" + this.divId).append("svg:svg").
			attr("width", r1 * 2).
			attr("height", r1 * 2).
			append("svg:g").
			attr("transform", "translate(" + r1 +"," + r1 + ")");
		
		var g;
		
		chord.matrix(this.matrix);
		
		g = svg.selectAll("g.group").
			data(chord.groups).
			enter().
			append("svg:g").
			attr("class", "group");
		
		g.append("svg:path").
			style("fill", function(d) { return fill(d.index); }).
			style("stroke", function(d) { return fill(d.index); }).
			attr("d", arc);
		
		g.append("svg:text").
			each(function(d) { d.angle = (d.startAngle + d.endAngle) / 2; }).
			attr("dy", ".35em").
			attr("text-anchor", function(d) { return d.angle > Math.PI ? "end" : null; }).
			attr("transform", function(d) {
				return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + 
					"translate(" + (r0 + 26) + ")" + 
					(d.angle > Math.PI ? "rotate(180)" : "");
			}).
			text(function(d) { return that.teamByIndex[d.index].teamName; });
		
		svg.selectAll("path.chord").
			data(chord.chords).
			enter().
			append("svg:path").
			attr("class", "chord").
			style("stroke", function(d) { return d3.rgb(fill(d.source.index)).darker(); }).
			style("fill", function(d) { return fill(d.source.index); }).
			attr("d", d3.svg.chord().radius(r0));
	}
});