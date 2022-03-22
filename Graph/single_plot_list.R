library(ggplot2)
library(hrbrthemes)
library(dplyr)
library(tidyr)
library(viridis)
library(ggridges)

Group_1 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD11)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD11)

Group_1  = rbind(Group_1 ,cLD_only)
Group_1  = rbind(Group_1 ,LD_only)

Group_1_data = data.frame(Group_1)
Group_1_data$Values =as.numeric(as.character(Group_1_data$Values))

p1 <- ggplot(Group_1_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_1_data$Values)-0.25, max(Group_1_data$Values)+.25))+
  theme_ipsum()

p1 = p1 +theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))

p1


Group_2 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD12)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD12)

Group_2  = rbind(Group_2 ,cLD_only)
Group_2  = rbind(Group_2 ,LD_only)

Group_2_data = data.frame(Group_2)
Group_2_data$Values =as.numeric(as.character(Group_2_data$Values))

p2 <- ggplot(Group_2_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_2_data$Values)-0.35, max(Group_2_data$Values)+.25))+
  theme_ipsum()

p2= p2+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))

p2

Group_3 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD13)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD13)

Group_3  = rbind(Group_3 ,cLD_only)
Group_3  = rbind(Group_3 ,LD_only)

Group_3_data = data.frame(Group_3)
Group_3_data$Values =as.numeric(as.character(Group_3_data$Values))

p3 <- ggplot(Group_3_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_3_data$Values)-0.35, max(Group_3_data$Values)+.25))+
  theme_ipsum()

p3=p3+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


Group_4 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD14)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD14)

Group_4  = rbind(Group_4 ,cLD_only)
Group_4  = rbind(Group_4 ,LD_only)

Group_4_data = data.frame(Group_4)
Group_4_data$Values =as.numeric(as.character(Group_4_data$Values))

p4 <- ggplot(Group_4_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_4_data$Values)-0.35, max(Group_4_data$Values)+.25))+
  theme_ipsum()

p4= p4+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


Group_5 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD22)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD22)

Group_5  = rbind(Group_5 ,cLD_only)
Group_5  = rbind(Group_5 ,LD_only)

Group_5_data = data.frame(Group_5)
Group_5_data$Values =as.numeric(as.character(Group_5_data$Values))

p5 <- ggplot(Group_5_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_5_data$Values)-0.35, max(Group_5_data$Values)+.25))+
  theme_ipsum()

p5=p5+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


p5

Group_6 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD23)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD23)

Group_6  = rbind(Group_6 ,cLD_only)
Group_6  = rbind(Group_6 ,LD_only)

Group_6_data = data.frame(Group_6)
Group_6_data$Values =as.numeric(as.character(Group_6_data$Values))

p6 <- ggplot(Group_6_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_6_data$Values)-0.35, max(Group_6_data$Values)+.25))+
  theme_ipsum()

p6 = p6+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))

Group_7 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD24)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD24)

Group_7  = rbind(Group_7 ,cLD_only)
Group_7  = rbind(Group_7 ,LD_only)

Group_7_data = data.frame(Group_7)
Group_7_data$Values =as.numeric(as.character(Group_7_data$Values))

p7 <- ggplot(Group_7_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_7_data$Values)-0.35, max(Group_7_data$Values)+.25))+
  theme_ipsum()

p7 = p7+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


Group_8 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD33)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD33)

Group_8  = rbind(Group_8 ,cLD_only)
Group_8  = rbind(Group_8 ,LD_only)

Group_8_data = data.frame(Group_8)
Group_8_data$Values =as.numeric(as.character(Group_8_data$Values))

p8 <- ggplot(Group_8_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_8_data$Values)-0.35, max(Group_8_data$Values)+.25))+
  theme_ipsum()

p8=p8+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


Group_9 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD34)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD34)

Group_9  = rbind(Group_9 ,cLD_only)
Group_9  = rbind(Group_9 ,LD_only)

Group_9_data = data.frame(Group_9)
Group_9_data$Values =as.numeric(as.character(Group_9_data$Values))

p9 <- ggplot(Group_9_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_9_data$Values)-0.35, max(Group_9_data$Values)+.25))+
  theme_ipsum()

p9=p9+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))


Group_10 <- data.frame(Type=character(),Values=numeric())

Type = rep("cLD",100)
cLD_only = cbind(Type,Values=eascLD44)

Type = rep("LD",250)
LD_only = cbind(Type,Values=easLD44)

Group_10  = rbind(Group_10 ,cLD_only)
Group_10  = rbind(Group_10 ,LD_only)

Group_10_data = data.frame(Group_10)
Group_10_data$Values =as.numeric(as.character(Group_10_data$Values))

p10 <- ggplot(Group_10_data, aes(x=Values, group=Type, fill=Type)) +
  geom_density(alpha=.55)+scale_x_continuous(limits = c(min(Group_10_data$Values)-0.35, max(Group_10_data$Values)+.25))+
  theme_ipsum()

p10=p10+theme(
  axis.text.x=element_blank(),
  axis.text.y=element_blank(),
  axis.ticks=element_blank(),
  axis.title.x=element_blank(),
  axis.title.y=element_blank(),
  plot.margin = unit(c(0.15, 0.15, 0.15, 0.15), "cm"),
  legend.position="none",
  panel.background=element_blank(),
  panel.border=element_blank(),
  panel.grid.major=element_blank(),
  panel.grid.minor=element_blank(),
  plot.background=element_blank())+ scale_fill_manual(values=c("#3b528b", "#5dc862"))



