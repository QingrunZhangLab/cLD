library("cowplot")

ggdraw() +
  draw_plot(p1, x = 0, y = .0, width = .25, height = .25) +
  draw_plot(p2, x = .25, y = .0, width = .25, height = .25) +
  draw_plot(p3, x = 0.50, y = 0, width = .25, height = 0.25) +
  draw_plot(p4, x = 0.75, y = 0, width = .25, height = 0.25) +
  draw_plot(p5, x = 0.25, y = 0.25, width = .25, height = 0.25) +
  draw_plot(p6, x = 0.50, y = 0.25, width = .25, height = 0.25) +
  draw_plot(p7, x = 0.75, y = 0.25, width = .25, height = 0.25) +
  draw_plot(p8, x = 0.50, y = 0.50, width = .25, height = 0.25) +
  draw_plot(p9, x = 0.75, y = 0.50, width = .25, height = 0.25) +
  draw_plot(p10, x = 0.75, y = 0.75, width = .25, height = 0.25) +
  draw_plot_label(label = c(""), size = 1,
                  x = c(0, 0.0, 0), y = c(0, 0, 0))

ggsave("eur.pdf", height=20, width=20)
