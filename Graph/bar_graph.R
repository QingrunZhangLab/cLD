library(ggplot2)
library(colortools)

tetradic("#8a9ec9")


bar = ggplot(data=EUR_graph, aes(x=cMAF, y=Ratio_of_Variances,fill = cMAF)) + 
  geom_bar(stat = "identity") + coord_flip() +
  scale_fill_manual("legend", values = c("#55b674", "#E69F00", "#56B4E9","#9e84f1"))

bar+scale_color_manual(values=c("#999999", "#E69F00", "#56B4E9","#5dc862"))


bar = bar+theme(legend.position="none")

bar + theme(panel.background = element_rect(fill = "white",
                                            colour = "white",
                                            size = 1.5, linetype = "solid"),
            panel.grid.major = element_line(size = 1.5, linetype = 'solid',
                                            colour = "white"), 
            panel.grid.minor = element_line(size = 1.25, linetype = 'solid',
                                            colour = "white"))
ggsave("eur_group.pdf", height=36, width=66,limitsize = FALSE)
bar
